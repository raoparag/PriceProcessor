package services;

import model.PriceReportItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Stream;

/**
 * Created by parag on 4/2/17.
 */
public class PriceWriter extends TimerTask {

    private static long lineSeeker = 0;
    private static boolean isEndOfFile;
    private static int ticker = 0;


    @Override
    public void run() {
        ticker++;
        String fileName = "data/data.txt";
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {

            isEndOfFile = true;
            //assuming that the file will always have data for 4 instruments at every second
            lines.skip(lineSeeker).limit(4).forEach(this::writeData);
            lineSeeker = lineSeeker + 4;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isEndOfFile) {
            //start reading from start of the file
            lineSeeker = 0;
            try (Stream<String> lines = Files.lines(Paths.get(fileName))) {

                lines.skip(lineSeeker).limit(4).forEach(this::writeData);
                lineSeeker = lineSeeker + 4;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeData(String line) {
        isEndOfFile = false;
        System.out.println(ticker + "\t" +line);
        String[] lineData = line.split(":");
        PriceProcessorService.writeData(lineData[0], Double.valueOf(lineData[1]));
        int persistFreq = PriceProcessorService.getPersistFreq().getOrDefault(lineData[0], 3);
        if (ticker % persistFreq == 0) PriceProcessorService.persistData(lineData[0]);
    }
}

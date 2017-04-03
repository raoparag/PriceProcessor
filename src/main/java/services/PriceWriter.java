package services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TimerTask;
import java.util.stream.Stream;

/**
 * Created by parag on 4/2/17.
 */
public class PriceWriter extends TimerTask {

    private static long lineSeeker = 0;
    private static boolean isEndOfFile;
    private static int runCounter = 0;


    @Override
    public void run() {
        runCounter++;
        String fileName = "data.txt";
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {

            isEndOfFile = true;
            //assuming that the file will always have data for 4 instruments at every second
            lines.skip(lineSeeker).limit(4).forEach(line -> {
                writeData(line);
                System.out.println(line);
                isEndOfFile = false;
            });
            lineSeeker = lineSeeker + 4;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isEndOfFile) {
            lineSeeker = 0;
            System.out.println("---------------------------");
            try (Stream<String> lines = Files.lines(Paths.get(fileName))) {

                lines.skip(lineSeeker).limit(4).forEach(line -> {
                    writeData(line);
                    System.out.println(line);
                });
                lineSeeker = lineSeeker + 4;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeData(String line) {
        String[] lineData = line.split(":");
        PriceProcessorService.writeData(lineData[0], Double.valueOf(lineData[1]));
        if (runCounter % 3 == 0) PriceProcessorService.persistData(lineData[0]);
    }
}

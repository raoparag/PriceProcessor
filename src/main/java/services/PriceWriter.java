package services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TimerTask;
import java.util.stream.Stream;

/**
 * Created by parag on 4/2/17.
 */
public class PriceWriter extends TimerTask {

    private static final Logger logger = LoggerFactory.getLogger(PriceWriter.class);
    private static long lineSeeker = 0;
    private static boolean isEndOfFile;
    private static int ticker = 0;


    @Override
    public void run() {
        ticker++;
        readFile();
    }

    public boolean readFile() {
        boolean readSuccess = true;
        String fileName = "data/data.txt";
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            isEndOfFile = true;
            //assuming that the file will always have data for 4 instruments at every second
            lines.skip(lineSeeker).limit(4).forEach(this::writeData);
            lineSeeker = lineSeeker + 4;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            readSuccess = false;
        }
        if (isEndOfFile) {
            //start reading from start of the file
            lineSeeker = 0;
            try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
                lines.skip(lineSeeker).limit(4).forEach(this::writeData);
                lineSeeker = lineSeeker + 4;
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                readSuccess = false;
            }
        }
        return readSuccess;
    }

    private void writeData(String line) {
        isEndOfFile = false;
        logger.info(ticker + "\t" + line);
        String[] lineData = line.split(":");
        PriceProcessorService.writeData(lineData[0], Double.valueOf(lineData[1]));
    }
}


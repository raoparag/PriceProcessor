package services;

import ds.PriceDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

/**
 * Created by parag on 4/2/17.
 */
public class PriceProcessorApplication {
    public static void main(String[] args) {

        //setup Database
        PriceDataSource.setup();

        //read and store data persist frequency
        String fileName = "data/persistFreq.txt";
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            Map<String, Integer> persistFreq = new HashMap<>();
            lines.forEach(line -> {
                String[] lineData = line.split(",");
                persistFreq.put(lineData[0], Integer.valueOf(lineData[1]));
            });
            PriceProcessorService.setPersistFreq(persistFreq);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Timer priceWriteTimer = new Timer();
        priceWriteTimer.scheduleAtFixedRate(new PriceWriter(), 100, 1000);

        final Timer priceReadTimer = new Timer();
        priceReadTimer.scheduleAtFixedRate(new PriceReader(), 100, 1000);

        final Timer mainTimer = new Timer();
        mainTimer.scheduleAtFixedRate(new TimerTask() {
            //run for 30 seconds
            int i = 30;

            @Override
            public void run() {
                i--;
                if (i < 0) {
                    priceWriteTimer.cancel();
                    priceReadTimer.cancel();
                    mainTimer.cancel();
                    PriceReader.printReport();
                }
            }
        }, 0, 1000);
    }
}

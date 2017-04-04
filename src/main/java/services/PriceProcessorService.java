package services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

/**
 * Created by parag on 4/3/17.
 */
public class PriceProcessorService {
    private static final Logger logger = LoggerFactory.getLogger(PriceProcessorService.class);
    //to store the data before persisting
    private static Map<String, ConcurrentLinkedQueue<Double>> dataStage = new ConcurrentHashMap<>();
    private static JdbcTemplate template;
    private static Map<String, Integer> persistFreq;

    public static void writeData(String instrument, double price) {
        ConcurrentLinkedQueue<Double> instrumentData = dataStage.getOrDefault(instrument, new ConcurrentLinkedQueue<>());
        instrumentData.add(price);
        dataStage.put(instrument, instrumentData);
    }

    public static void setup(){
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
            logger.error(e.getMessage(), e);
        }
    }

    public static Map<String, Integer> getPersistFreq() {
        return persistFreq;
    }

    public static void setPersistFreq(Map<String, Integer> persistFreq) {
        PriceProcessorService.persistFreq = persistFreq;
    }

    public static Map<String, ConcurrentLinkedQueue<Double>> getDataStage() {
        return dataStage;
    }
}


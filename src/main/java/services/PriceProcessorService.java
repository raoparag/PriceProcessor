package services;

import ds.PriceDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by parag on 4/3/17.
 */
//TODO: data structure should be synchronized
public class PriceProcessorService {
    //to store the data before persisting
    private static Map<String, LinkedList<Double>> dataStage = new HashMap<>();
    private static JdbcTemplate template;

    public static void writeData(String instrument, double price) {
        LinkedList<Double> instrumentData = dataStage.getOrDefault(instrument, new LinkedList<>());
        instrumentData.push(price);
        dataStage.put(instrument, instrumentData);
    }

    public static void persistData(String instrument) {
        template = new JdbcTemplate(PriceDataSource.get());
        dataStage.get(instrument).forEach(price -> {
            template.execute("insert into price_data values ('" + instrument + "'," + price + ")");
        });
        dataStage.remove(instrument);
    }
}

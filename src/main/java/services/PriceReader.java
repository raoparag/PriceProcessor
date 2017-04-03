package services;

import ds.PriceDataSource;
import model.PriceData;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.TimerTask;

/**
 * Created by parag on 4/2/17.
 */
public class PriceReader extends TimerTask {

    @Override
    public void run() {
        JdbcTemplate template = new JdbcTemplate(PriceDataSource.get());
        List<PriceData> priceDataList = template.query("Select instrument, price from price_data", (rs, rowNum) -> {
            PriceData priceData = new PriceData();
            priceData.setInstrument(rs.getString("instrument"));
            priceData.setPrice(rs.getDouble("price"));
            return priceData;
        });
        System.out.println("persisted data size - " + priceDataList.size());
    }
}

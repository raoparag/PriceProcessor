package services;

import db.PriceDatabase;
import model.PriceReportItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by parag on 4/2/17.
 */
public class PriceReader extends TimerTask {
    private static final Logger logger = LoggerFactory.getLogger(PriceReader.class);
    private static List<PriceReportItem> priceReportItemList = new ArrayList<>();
    private static int ticker = 0;
    private static JdbcTemplate template = null;

    @Override
    public void run() {
        ticker++;
        readData();
        updateReport();
    }

    public static void readData() {
        Map<String, ConcurrentLinkedQueue<Double>> dataStage = PriceProcessorService.getDataStage();
        dataStage.forEach((instrument, priceList) -> {
            int persistFreq = PriceProcessorService.getPersistFreq().getOrDefault(instrument, 3);
            if (ticker % persistFreq == 0) {
                persistData(instrument, priceList);
            }
        });
    }

    public static void persistData(String instrument, ConcurrentLinkedQueue<Double> priceList) {
        if (template == null) template = new JdbcTemplate(PriceDatabase.get());
        final int[] counter = {0};
        priceList.forEach(price -> {
            template.execute("insert into price_data (ticker, instrument, price) values (" + ticker + ",'" + instrument + "'," + price + ")");
            counter[0]++;
            priceList.remove(price);
        });
        logger.info("instrument {} persisted with {} prices", instrument, counter[0]);
    }


    public void updateReport() {
        Set<String> instrumentSet = new HashSet<>();
        //assuming that the second highest persisted price is needed for each instrument
        if (template == null) template = new JdbcTemplate(PriceDatabase.get());
        List<PriceReportItem> resultList = template.query("SELECT instrument, max(price) AS max_price FROM price_data a WHERE price NOT IN (SELECT max(price) FROM price_data b WHERE b.instrument = a.instrument) GROUP BY instrument", (rs, rowNum) -> {
            String instrument = rs.getString("instrument");
            Double avg10 = template.queryForObject("select avg(price) as avg_price from price_data where ticker > " + (ticker - 10) + " and instrument='" + instrument + "'", Double.class);
            PriceReportItem priceReportItem = new PriceReportItem();
            priceReportItem.setTicker(ticker);
            priceReportItem.setInstrument(rs.getString("instrument"));
            priceReportItem.setSecondHighPrice(rs.getDouble("max_price"));
            priceReportItem.setAvgPrice10Sec(avg10);
            instrumentSet.add(priceReportItem.getInstrument());
            return priceReportItem;
        });
        if (resultList.size() < PriceProcessorService.getPersistFreq().size()) {
            PriceProcessorService.getPersistFreq().forEach((instrument, persistFreq) -> {
                //add missing instruments for which the second highest price was not available
                if (!instrumentSet.contains(instrument))
                    //-1 symbolizing data not available
                    resultList.add(new PriceReportItem(ticker, instrument, -1, -1));
            });
        }
        priceReportItemList.addAll(resultList);
    }

    public static List<PriceReportItem> getPriceReportItemList() {
        return priceReportItemList;
    }
}


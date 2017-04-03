package services;

import ds.PriceDataSource;
import model.PriceReportItem;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

/**
 * Created by parag on 4/2/17.
 */
public class PriceReader extends TimerTask {
    private static List<PriceReportItem> priceReportItemList = new ArrayList<>();
    private static int ticker = 0;
    private JdbcTemplate template = null;

    @Override
    public void run() {
        ticker++;
        updateReport();
    }

    private void updateReport() {

        Set<String> instrumentSet = new HashSet<>();
        //assuming that the second highest persisted price is needed for each instrument
        if (template == null) template = new JdbcTemplate(PriceDataSource.get());
        List<PriceReportItem> resultList = template.query("select instrument, max(price) as max_price from price_data a where price not in (select max(price) from price_data b where b.instrument = a.instrument) group by instrument", (rs, rowNum) -> {
            PriceReportItem priceReportItem = new PriceReportItem();
            priceReportItem.setTicker(ticker);
            priceReportItem.setInstrument(rs.getString("instrument"));
            priceReportItem.setSecondHighPrice(rs.getDouble("max_price"));
            instrumentSet.add(priceReportItem.getInstrument());
            return priceReportItem;
        });
        if (resultList.size() > 0) {
            priceReportItemList.addAll(resultList);
        } else {
            //-1 symbolizing data not available
            PriceProcessorService.getPersistFreq().forEach((instrument, persistFreq) -> {
                //add missing instruments for which the second highest price was not available
                if (!instrumentSet.contains(instrument))
                    priceReportItemList.add(new PriceReportItem(ticker, instrument, -1, -1));
            });
        }
    }

    public static void printReport() {
        System.out.println("\n\n\n-----------------------");
        System.out.println("Instrument Price Report");
        System.out.println("-----------------------");
        System.out.println("Ticker\t\tInstrument\t\tSecond Highest Price\t\t\tAvg price in last 10 sec");
        priceReportItemList.forEach(System.out::println);
    }
}

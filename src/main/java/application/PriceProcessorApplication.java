package application;

import db.PriceDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.PriceProcessorService;
import services.PriceReader;
import services.PriceReport;
import services.PriceWriter;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by parag on 4/2/17.
 */
public class PriceProcessorApplication {
    private static final Logger logger = LoggerFactory.getLogger(PriceProcessorApplication.class);

    public static void main(String[] args) {

        //setup Database
        PriceDatabase.setup();

        //setup the persist frequency from data/persistFreq.txt file
        PriceProcessorService.setup();

        final Timer priceWriteTimer = new Timer();
        final Timer priceReadTimer = new Timer();
        priceWriteTimer.scheduleAtFixedRate(new PriceWriter(), 100, 1000);
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
                    final Timer priceReportTimer = new Timer();
                    priceReportTimer.schedule(new PriceReport(priceReportTimer), 500);
                }
            }
        }, 0, 1000);
    }
}


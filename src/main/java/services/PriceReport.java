package services;

import db.PriceDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by parag on 4/4/17.
 */
public class PriceReport extends TimerTask {
    private static final Logger logger = LoggerFactory.getLogger(PriceReport.class);

    private Timer invokeTimer;

    public PriceReport(Timer timer) {
        invokeTimer = timer;
    }

    @Override
    public void run() {
        //check for any left over data in dataStage
        PriceReader.readData();
        printReport();
        PriceDatabase.shutdown();
        invokeTimer.cancel();
    }

    public static void printReport() {
        logger.info("\n\n\n");
        logger.info("--------------------------------------------------------------------------------------------");
        logger.info("                                 Instrument Price Report");
        logger.info("--------------------------------------------------------------------------------------------");
        logger.info("Ticker\t\tInstrument\t\tSecond Highest Price\t\t\tAvg price in last 10 sec");
        PriceReader.getPriceReportItemList().forEach(x -> logger.info(x.toString()));
    }
}


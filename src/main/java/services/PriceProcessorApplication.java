package services;

import ds.PriceDataSource;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by parag on 4/2/17.
 */
public class PriceProcessorApplication {
    public static void main(String[] args) {

        //setup Database
        PriceDataSource.setup();

        final Timer priceWriteTimer = new Timer();
        priceWriteTimer.scheduleAtFixedRate(new PriceWriter(), 0, 1000);

        final Timer priceReadTimer = new Timer();
        priceReadTimer.scheduleAtFixedRate(new PriceReader(), 0, 1000);

        final Timer mainTimer = new Timer();
        mainTimer.scheduleAtFixedRate(new TimerTask() {
            //run for 30 seconds
            int i = 30;

            @Override
            public void run() {
                System.out.println(i--);
                if (i < 0) {
                    priceWriteTimer.cancel();
                    priceReadTimer.cancel();
                    mainTimer.cancel();
                }
            }
        }, 0, 1000);
    }
}

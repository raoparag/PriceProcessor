package services;

import java.util.Timer;

/**
 * Created by parag on 4/2/17.
 */
public class PriceProcessor {
    public static void main(String[] args) {
        Timer priceWriteTimer = new Timer();
        priceWriteTimer.scheduleAtFixedRate(new PriceWriter(), 0, 1000);
    }
}

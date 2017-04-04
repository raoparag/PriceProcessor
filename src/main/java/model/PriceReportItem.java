package model;

/**
 * Created by parag on 4/3/17.
 */
public class PriceReportItem {
    private int ticker;
    private String instrument;
    private double secondHighPrice;
    private double avgPrice10Sec;

    public int getTicker() {
        return ticker;
    }

    public void setTicker(int ticker) {
        this.ticker = ticker;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public double getSecondHighPrice() {
        return secondHighPrice;
    }

    public void setSecondHighPrice(double secondHighPrice) {
        this.secondHighPrice = secondHighPrice;
    }

    public double getAvgPrice10Sec() {
        return avgPrice10Sec;
    }

    public void setAvgPrice10Sec(double avgPrice10Sec) {
        this.avgPrice10Sec = avgPrice10Sec;
    }

    @Override
    public String toString() {
        return ticker +
                "\t\t" + instrument +
                "\t\t\t" + ((secondHighPrice == -1) ? "Not enough data" : secondHighPrice) +
                "\t\t\t\t\t\t" + ((avgPrice10Sec == -1) ? "No data" : avgPrice10Sec);
    }

    public PriceReportItem() {
    }

    public PriceReportItem(int ticker, String instrument, double secondHighPrice, double avgPrice10Sec) {
        this.ticker = ticker;
        this.instrument = instrument;
        this.secondHighPrice = secondHighPrice;
        this.avgPrice10Sec = avgPrice10Sec;
    }
}


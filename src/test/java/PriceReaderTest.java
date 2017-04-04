import db.PriceDatabase;
import org.junit.Assert;
import org.junit.Test;
import services.PriceProcessorService;
import services.PriceReader;
import services.PriceReport;
import services.PriceWriter;

/**
 * Created by parag on 4/4/17.
 */
public class PriceReaderTest {
    @Test
    public void priceReportTest() {

        //setup Database
        PriceDatabase.setup();

        //setup the presist frequency
        PriceProcessorService.setup();

        PriceWriter priceWriter = new PriceWriter();
        for (int i = 0; i < 10; i++) {
            priceWriter.readFile();
        }
        PriceReader priceReader = new PriceReader();
        PriceProcessorService.getDataStage().forEach(PriceReader::persistData);
        priceReader.updateReport();

        Assert.assertTrue(PriceReader.getPriceReportItemList().size() > 0);
        PriceReport.printReport();
    }
}


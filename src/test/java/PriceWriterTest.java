import db.PriceDatabase;
import org.junit.Assert;
import org.junit.Test;
import services.PriceProcessorService;
import services.PriceWriter;

/**
 * Created by parag on 4/4/17.
 */
public class PriceWriterTest {
    @Test
    public void readDataFile() {

        //setup Database
        PriceDatabase.setup();

        //setup the presist frequency
        PriceProcessorService.setup();

        Assert.assertTrue((new PriceWriter()).readFile());
    }
}


import db.PriceDatabase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

/**
 * Created by parag on 4/4/17.
 */
public class PriceDatabaseTest {

    @Test
    public void testDB(){
        EmbeddedDatabase db = PriceDatabase.get();
        Assert.assertNotNull(db);
    }
}


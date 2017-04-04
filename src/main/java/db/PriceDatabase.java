package db;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * Created by parag on 4/3/17.
 */
public class PriceDatabase {
    private static EmbeddedDatabase db = null;

    public static void setup() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("schema.sql")
                .build();
    }

    public static EmbeddedDatabase get() {
        if (db == null) setup();
        return db;
    }

    public static void shutdown(){
        db.shutdown();
    }
}


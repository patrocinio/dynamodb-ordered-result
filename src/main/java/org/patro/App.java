package org.patro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String... args) {
        logger.info("Application starts");

        Handler.trimTable();

//        PutItemThread putItem = new PutItemThread();
//        putItem.start();

        GetItemThread getItem = new GetItemThread();
        getItem.start();
        
        logger.info("Application ends");
    }
}

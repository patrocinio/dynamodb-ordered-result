package org.patro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String... args) {
        logger.info("Application starts");

        Handler.trimTable();
        SequenceNumber seqNo = new SequenceNumber();

        PutItemThread putItem = new PutItemThread(seqNo);
        putItem.start();

        GetItemThread getItem = new GetItemThread(seqNo);
        getItem.start();

        try {
            putItem.join();
            getItem.join();
        } catch (InterruptedException e) {
        }

        Handler.trimTable();

        logger.info("Application ends");
    }
}

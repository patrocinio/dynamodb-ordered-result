package org.patro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String... args) {
        logger.info("Application starts");

        Handler.trimTable();
        SequenceNumber seqNo = new SequenceNumber();
        PutItemThread[] putThreads = new PutItemThread[Configuration.NO_OF_THREADS];
        GetItemThread[] getThreads = new GetItemThread[Configuration.NO_OF_THREADS];

        for (int i = 0; i < Configuration.NO_OF_THREADS; i++)
        {
            PutItemThread putItem = new PutItemThread(seqNo);
            putItem.start();
            putThreads[i] = putItem;

            GetItemThread getItem = new GetItemThread(seqNo);
            getItem.start();
            getThreads[i] = getItem;
        }
        
        try {
            for (int i = 0; i < Configuration.NO_OF_THREADS; i++) {
                putThreads[i].join();
                getThreads[i].join();
            }
        } catch (InterruptedException e) {
        }

        Handler.trimTable();

        logger.info("Application ends");
    }
}

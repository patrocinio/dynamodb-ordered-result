package org.patro;

public class PutItemThread extends Thread {
    public void run() {
        for (int i = 0; i < Configuration.ITERATIONS; i++)
        {
            int seqNo = SequenceNumber.incSeqNo();

            Handler.sendRequest(seqNo);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

package org.patro;

public class GetItemThread extends Thread {
    public void run() {
        for (int i = 0; i < Configuration.ITERATIONS; i++)
        {

            int readNo = Handler.sendReadRequest();
            int seqNo = SequenceNumber.getSeqNo();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

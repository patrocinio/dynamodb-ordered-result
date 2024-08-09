package org.patro;

public class GetItemThread extends Thread {
    public void run() {
        for (int i = 0; i < Configuration.ITERATIONS; i++)
        {

            int readNo = Handler.sendReadRequest();
            int seqNo = SequenceNumber.getSeqNo();
            if (readNo != seqNo) {
                System.err.format("Error: Read %s but expected %s.\n", readNo, seqNo);
                System.exit(1);
                   
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

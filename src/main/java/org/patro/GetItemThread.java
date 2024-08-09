package org.patro;

public class GetItemThread extends Thread {
    private SequenceNumber seqNo;

    public GetItemThread(SequenceNumber seqNo) {
        this.seqNo = seqNo;
    }

    public void run() {
        for (int i = 0; i < Configuration.ITERATIONS; i++)
        {
            synchronized(seqNo) {
                int readNo = Handler.sendReadRequest();
                int number = seqNo.getSeqNo();
                if (readNo != number) {
                    System.err.format("Error: Read %s but expected %s.\n", readNo, number);
                    System.exit(1);
                    
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

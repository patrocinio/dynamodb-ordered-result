package org.patro;

public class GetItemThread extends Thread {
    private SequenceNumber seqNo;

    public GetItemThread(SequenceNumber seqNo) {
        this.seqNo = seqNo;
    }

    public void run() {
        for (int i = 0; i < Configuration.ITERATIONS; i++)
        {
            int readNo;
            int number;
            synchronized(seqNo) {
                readNo = Handler.sendReadRequest();
                number = seqNo.getSeqNo();
            }
            if (readNo != number) {
                String threadName = seqNo.getThreadName();
                System.err.format("Error: Read %s but expected %s set by thread %s.\n", readNo, number, threadName);
                System.exit(1);
                
            }
            try {
                Thread.sleep((long) (Configuration.DELAY*Math.random()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

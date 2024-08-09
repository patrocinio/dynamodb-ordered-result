package org.patro;

public class PutItemThread extends Thread {
    private SequenceNumber seqNo;

    public PutItemThread(SequenceNumber seqNo) {
        this.seqNo = seqNo;
    }
    
    public void run() {
        for (int i = 0; i < Configuration.ITERATIONS; i++)
        {
            synchronized(seqNo) {
                int number = seqNo.getSeqNo();

                number++;
                Handler.sendWriteRequest(number);
                seqNo.setSeqNo(number);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

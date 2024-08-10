package org.patro;

public class SequenceNumber {
    private int counter = 0;
    private String threadName;

    public int getSeqNo() {
        return counter;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setSeqNo(int c) {
        counter = c;
        threadName = Thread.currentThread().getName();
    }
}

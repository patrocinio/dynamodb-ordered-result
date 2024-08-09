package org.patro;

public class SequenceNumber {
    private static int counter = 0;

    public int getSeqNo() {
        return counter;
    }

    public void setSeqNo(int c) {
        counter = c;
    }
}

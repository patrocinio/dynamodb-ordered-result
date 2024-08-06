package org.patro;

public class SequenceNumber {
    private static int counter = 0;

    public static int getSeqNo() {
        return counter;
    }

    public static int incSeqNo() {
        counter++;
        return counter;
    }
}

package utils;

import types.ThreadedBufferedReader;

import java.io.BufferedReader;
import java.io.IOException;

// This class takes a buffered reader and continuously retrieves the latest data (Dirty, no synchronization)
public class MinimumDelayReader extends ThreadedBufferedReader {

    // if the input queue has more than 5 unprocessed element, will start to skip input
    // delay can be calculated as = MAX_DELAY / (polling rate) = ~10 ms (for 500hz polling rate)
    private static final int MAXIMUM_DELAY_ALLOWED = 5;

    public MinimumDelayReader(BufferedReader in) {
        super(in);
    }

    @Override
    public String getInput() {
        if (getInputQueueSize() > MAXIMUM_DELAY_ALLOWED) {
//            System.out.println("Max. Delayed exceeded, skipping inputs");
            int i = 0;
            int count = (int) (Math.log(getInputQueueSize()) / Math.log(2));
            while (i < count) {
                super.getInput();
                i++;
            }
        }
        return super.getInput();
    }
}

package utils;

import types.ThreadedBufferedReader;

import java.io.BufferedReader;
import java.io.IOException;

// This class takes a buffered reader and continuously retrieves the latest data
public class MinimumDelayReader extends ThreadedBufferedReader {

    // if the input queue has more than  unprocessed element, will start to skip input
    // delay can be calculated as = MAX_DELAYED_INPUT_COUNT / (polling rate in ms) = 10 / 0.5 ms = ~20ms (for 500hz polling rate)
    private static final int MAXIMUM_DELAYED_INPUT_ALLOWED = 10;

    public MinimumDelayReader(BufferedReader in) {
        super(in);
    }

    @Override
    public String getInput() {
        int queueSize = getInputQueueSize();
        if (queueSize > MAXIMUM_DELAYED_INPUT_ALLOWED) {
//            System.out.println("Max. Delayed exceeded, skipping inputs");
//            int i = 0;
            return clearAllReturnLast();
        }
        return super.getInput();
    }
}

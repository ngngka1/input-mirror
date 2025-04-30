package utils;

import types.BackgroundTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

// This class takes a buffered reader and continuously retrieves the latest data (Dirty, no synchronization)
public class LatestInputProvider extends BackgroundTask {
    private static final int MAXIMUM_DELAY_ALLOWED = 5; // if the input queue has more than 5 unprocessed element, will start to skip input
                                                        // delay can be calculated as = MAX_DELAY / (polling rate) = ~10 ms (for 500hz polling rate)
    private BufferedReader in;
    private volatile String latestLine;
    private volatile Queue<String> inputQueue;

    public LatestInputProvider(BufferedReader in) {
        this.latestLine = "";
        this.in = in;
        this.inputQueue = new LinkedList<>();
    }

    private void setLatestLine(String x) {
        latestLine = x;
    }
    public String readLatestLine() {
//        String temp = null;
        if (inputQueue.size() > MAXIMUM_DELAY_ALLOWED) {
            System.out.println("Max. Delayed exceeded, skipping inputs");
            int i = 0;
            int count = (int) (Math.log(inputQueue.size()) / Math.log(2));
            while (i < count) {
                inputQueue.poll();
                i++;
            }
        }
//        latestLine = null;
        return inputQueue.poll();
    }

    @Override
    public void run() {
        try {
            while (!isTerminated()) {
                String input = in.readLine();
                inputQueue.add(input);
//                setLatestLine(input);
            }
        } catch (IOException e) {

        }
    }



//    public synchronized String readLatestLine() {
//        synchronized (latestLine) {
//            return latestLine.getData();
//        }
//    }
}

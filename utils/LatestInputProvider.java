package utils;

import types.BackgroundTask;

import java.io.BufferedReader;
import java.io.IOException;

// This class takes a buffered reader and continuously retrieves the latest data (Dirty, no synchronization)
public class LatestInputProvider extends BackgroundTask {

    private BufferedReader in;
    private volatile String latestLine;

    public LatestInputProvider(BufferedReader in) {
        this.latestLine = "";
        this.in = in;
    }

    private void setLatestLine(String x) {
        latestLine = x;
    }
    public String readLatestLine() {
        String temp = latestLine;
        latestLine = null;
        return temp;
    }

    @Override
    public void run() {
        try {
            while (!isTerminated()) {
                String input = in.readLine();
                setLatestLine(input);
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

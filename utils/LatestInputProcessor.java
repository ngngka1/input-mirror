package utils;

import types.BackgroundTask;
import types.BackgroundTaskStatic;

import java.io.BufferedReader;
import java.io.IOException;

// This class takes a buffered reader and continuously retrieves the latest data
public class LatestInputProcessor extends BackgroundTask {

    private BufferedReader in;
    private static class AtomicString {
        String data;
        AtomicString(String x) {
            data = x;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }
    }

    private final AtomicString latestLine;

    public LatestInputProcessor(BufferedReader in) {
        this.latestLine = new AtomicString("");
        this.in = in;
    }

    private void setLatestLine(String x) {
        synchronized (latestLine) {
            latestLine.setData(x);
        }
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

    public String readLatestLineDirty() {
        return latestLine.getData();
    }

    public synchronized String readLatestLine() {
        synchronized (latestLine) {
            return latestLine.getData();
        }
    }
}

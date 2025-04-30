package utils;

import types.BackgroundTaskStatic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

// separating this from Application.java in case the app may receive input in other forms in the future
public class InputProvider extends BackgroundTaskStatic {
    private static final Queue<String> inputQueue = new LinkedList<>();

    public static void init() {
        new Thread(new InputProvider()).start();
    }

    // the logic to get input should entirely be here
    @Override
    public void run() {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println("ConsoleInputReadTask run() called.");
        CloseableInterrupter.hook(br);
        String input;
        while (!isTerminated()) {
            try {
                while (!br.ready()) {
                    Thread.sleep(200);
                }
                input = br.readLine();
            } catch (IOException | InterruptedException e) {
                break;
            }
            synchronized (inputQueue) {
                inputQueue.add(input);
                inputQueue.notifyAll();
            }
        }
//        System.out.println("InputProvider terminated"); // for debug
    }

    public static String getNonBlockingInput() {
        synchronized (inputQueue) {
            if (inputQueue.isEmpty()) {
                return "";
            }
            return inputQueue.remove();
        }
    }

    public static String getInput()
    {
        synchronized (inputQueue) {
            try {
                while (inputQueue.isEmpty()) {
                    inputQueue.wait();
                }
                return inputQueue.remove();
            } catch (InterruptedException e) {
                //
                return "";
            }
        }
    }

    public static String popUntilLatestInput() {
        synchronized (inputQueue) {
            try {
                while (inputQueue.isEmpty()) {
                    inputQueue.wait();
                }

                String x = null;
                while (inputQueue.peek() != null) {
                    x = inputQueue.remove();
                }
                return x;
            } catch (InterruptedException e) {
                //
                return "";
            }
        }
    }

}

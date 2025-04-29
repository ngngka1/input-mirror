package utils;

import types.BackgroundTask;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

// separating this from Application.java in case the app may receive input in other forms in the future
public class InputProvider extends BackgroundTask {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Queue<String> inputQueue = new LinkedList<>();

    public static void init() {
        new Thread(new InputProvider()).start();
    }

    // the logic to get input should entirely be here
    @Override
    public void run() {
        while (!isTerminated() && scanner.hasNext()) {
            synchronized (inputQueue) {
                inputQueue.add(scanner.nextLine());
            }
            notifyAll();
        }
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

}

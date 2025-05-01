package types;

import utils.CloseableInterrupter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class ThreadedBufferedReader extends BackgroundTask implements Readable {

    private volatile Queue<String> inputQueue;
    private final BufferedReader br;

    public ThreadedBufferedReader(BufferedReader br) {
        this.br = br;
        inputQueue = new LinkedList<>();
    }

    public ThreadedBufferedReader(InputStream inputStream) {
        this(new BufferedReader(new InputStreamReader(inputStream)));
    }

    @Override
    public void run() {
        CloseableInterrupter.hook(br);
        String input;
        while (!isTerminated()) {
            try {
                while (!br.ready()) {
//                    Thread.sleep(50);
                }
                input = br.readLine();
            } catch (IOException e) {
                break;
            }
            inputQueue.add(input);
        }

//        System.out.println("Terminated threaded scanner");
    }

    public String getInputNonBlocking() {
        String x = inputQueue.poll();
        return x == null ? "" : x;
    }

    @Override
    public String getInput()
    {
        String x = null;
        do {
            x = inputQueue.poll();
        } while (x == null);
        return x;
    }

    public int getInputQueueSize() {
        return inputQueue.size();
    }
}

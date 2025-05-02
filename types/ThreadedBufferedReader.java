package types;

import utils.CloseableInterrupter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.util.Queue;
import org.apache.commons.collections4.queue.CircularFifoQueue;

public class ThreadedBufferedReader extends BackgroundTask implements Readable {

    private volatile CircularFifoQueue<String> inputQueue;
    private final BufferedReader br;

    public ThreadedBufferedReader(BufferedReader br) {
        this.br = br;
        inputQueue = new CircularFifoQueue<String>(500);
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
            if (inputQueue.size() >= 500) {
                inputQueue.clear();
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

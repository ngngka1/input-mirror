package types;

public abstract class BackgroundTask implements Runnable {
    private static boolean paused = false;
    private static boolean terminated = false;

    public synchronized static boolean isPaused() {return paused;}
    public synchronized static boolean isTerminated() {return terminated;}
    public synchronized static void pause() {
        paused = true;
    }
    public synchronized static void unPause() {
        paused = false;
    }

    public synchronized static void terminate() {
        terminated = true;
    }
}

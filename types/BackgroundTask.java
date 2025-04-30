package types;

public abstract class BackgroundTask implements Runnable {
    private  boolean paused = false;
    private  boolean terminated = false;

    public synchronized  boolean isPaused() {return paused;}
    public synchronized  boolean isTerminated() {return terminated;}
    public synchronized  void pause() {
        paused = true;
    }
    public synchronized  void unPause() {
        paused = false;
    }

    public synchronized  void terminate() {
        terminated = true;
    }
}

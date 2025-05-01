package types;

public abstract class BackgroundTask implements Runnable {
    // for all subclasses, if a socket/reader/any closeables are used, the function
    // closeableInterrupter.hook(socket) has to be called, in order to interrupt and terminate
    // threads which are blocked due to sockets etc., like:
    //
    // socket.receive()
    // reader.ready()
    //

    private boolean paused = false;
    private boolean terminated = false;

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

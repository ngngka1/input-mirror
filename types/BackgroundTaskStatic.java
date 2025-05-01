package types;

public abstract class BackgroundTaskStatic implements Runnable {
    // for all subclasses, if a socket/reader/any closeables are used, the function
    // closeableInterrupter.hook(socket) has to be called, in order to interrupt and terminate
    // threads which are blocked due to sockets etc., like:
    //
    // socket.receive()
    // reader.ready()
    //

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

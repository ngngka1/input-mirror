package types;

public abstract class BackgroundTask implements Runnable {
    private static boolean paused = false;
    private static boolean terminated = false;

    public static boolean isPaused() {return paused;}
    public static boolean isTerminated() {return terminated;}
    public static void pause() {
        paused = true;
    }
    public static void unPause() {
        paused = false;
    }

    public static void termiante() {
        terminated = true;
    }
}

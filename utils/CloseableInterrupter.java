package utils;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CloseableInterrupter {
    private static List<Closeable> closeables = new ArrayList<>();



    public static void hook(Closeable socket) {
        closeables.add(socket);
    }

    public static void closeAll() {
        for (Closeable c : closeables) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

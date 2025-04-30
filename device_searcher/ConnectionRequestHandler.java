package device_searcher;

import types.BackgroundTaskStatic;
import types.Device;
import types.DeviceConnection;
import utils.CloseableInterrupter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

public class ConnectionRequestHandler extends BackgroundTaskStatic {
    private final int tcpPort;
    private final static List<DeviceConnection> connectionRequestQueue = new ArrayList<>();

    public static List<DeviceConnection> getConnectionRequests() {
        return connectionRequestQueue;
    }
    public static void clearClosedRequests() {
        synchronized (connectionRequestQueue) {
            for (int i = 0; i < connectionRequestQueue.size(); i++) {
                if (connectionRequestQueue.get(i).clientSocket.isClosed()) {
                    connectionRequestQueue.remove(i);
                    i--;
                } else {
                    try {
                        Socket clientSocket = connectionRequestQueue.get(i).clientSocket;
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        clientSocket.setSoTimeout(150);
                        String response = in.readLine();
//                        if (response == null) {
//                            connectionRequestQueue.remove(i);
//                            i--;
//                        }
                    } catch (SocketTimeoutException e) {
                        //
                    } catch (IOException e) {
                        connectionRequestQueue.remove(i);
                        i--;
                    }
                }
            }
        }
    }

    public static void init(int tcpPort) {
        new Thread(new ConnectionRequestHandler(tcpPort)).start();
    }

    public ConnectionRequestHandler(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(this.tcpPort)) {
            CloseableInterrupter.hook(serverSocket);
            while (!isTerminated()) {
                if (isPaused()) {continue;}
                Socket clientSocket;
                try {
                    clientSocket = serverSocket.accept();
                } catch (SocketException e) {
                    break;
                }
//                clientSocket.setSoTimeout(3000);
                InetAddress clientAddress = clientSocket.getInetAddress();
                synchronized (connectionRequestQueue) {
//                    clearClosedRequests();
                    connectionRequestQueue.add(new DeviceConnection(clientSocket, new Device(clientAddress.getHostName(), clientAddress, clientSocket.getPort())));
                }
            }

//            System.out.println("ConnectionRequestHandler terminated"); // for debug
        }
        catch (IOException e) {
            System.err.println(e);
        }

    }
}

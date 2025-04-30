package controller;

import device_searcher.BroadcastHandler;
import types.Device;
import types.DeviceConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionController {
//    private final int tcpPort;
    private final List<DeviceConnection> connectionRequestQueue;


    public ConnectionController(List<DeviceConnection> connectionRequestQueue) {
//        this.tcpPort = tcpPort;
        this.connectionRequestQueue = connectionRequestQueue;
    }

    public List<Device> getRequestedDevices() {
//        System.out.println();
        List<Device> arr = new ArrayList<>();
        synchronized (connectionRequestQueue) {
            for (int i = 0; i < connectionRequestQueue.size(); i++) {
                Device device = connectionRequestQueue.get(i).device;
                arr.add(device);
                String hostname = device.getHostname();
                String address = device.getAddress().toString();
                System.out.println(i + ". " + hostname + " " + address);
            }
        }
        return arr;
    }

    // for sender, initiate connection
    public Socket connect(Device targetDevice) {
        Socket clientSocket = null;
        try {
            try {
                clientSocket = new Socket(targetDevice.getAddress(), targetDevice.getTcpPort());
                System.out.println("Waiting for the target device to accept the connection...");
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                clientSocket.setSoTimeout(60 * 1000); // wait for 60 seconds before timing out
                if (!(in.readLine().equals("SYNACK"))) {
                    System.out.println("Target device declined the connection.");
                    return null;
                }
                System.out.println("target device accepted the connection, now preparing to send data...");
                return clientSocket;
            } catch (SocketTimeoutException e) {
                System.out.println("connection timed out while waiting for target device's acknowledgement.");
                if (clientSocket != null)
                    clientSocket.close();
                return null;
            }
        }
         catch (IOException e) {
            System.out.println("e:" + e.toString());
            System.out.println("Target device has already closed the connection.");
            return null;
        }
    }

    // for receiver
    private Socket getSocketFromConnectionRequests(Device targetDevice) {
        Socket clientSocket = null;
        synchronized (connectionRequestQueue) {
            for (DeviceConnection curr : connectionRequestQueue) {
                if (curr.device.equals(targetDevice)) {
                    clientSocket = curr.clientSocket;
                    break;
                }

            }
            if (clientSocket == null) {
                System.out.println("socket to this device is not found");
                return null;
            }
            if (clientSocket.isClosed()) {
                System.out.println("socket to this device is closed");
                return null;
            }
            return clientSocket;
        }
    }



    public Socket acceptConnection(Device targetDevice) {
        Socket clientSocket = getSocketFromConnectionRequests(targetDevice);
        if (clientSocket == null)
            return null;

        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("SYNACK");
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientSocket.setSoTimeout(5000); // wait for 5 seconds to let sender ACK the connection accept
            String response = in.readLine();
            if (response == null) {
                System.out.println("sender device has already closed the connection (possibly due to a timeout)");
                clientSocket.close();
                return null;
            }
            if (!response.equals("ACK")) {
                System.out.println("sender device doesn't acknowledge the connection, connection terminating.");
                clientSocket.close();
                return null;
            }
            clientSocket.setSoTimeout(0); // reset timeout
        } catch (SocketTimeoutException e){
            System.out.println("sender device has not acknowledged the connection in expected time, connection terminating.");
            return null;
        } catch (IOException e) {
            System.out.println("Failed to reach sender device, the connection has already been closed by the other device");
            return null;
        }

        System.out.println("Connected to device: " + targetDevice.getHostname() + ", IP: " + targetDevice.getAddress());

        return clientSocket;
    }

    public void declineConnection(Device targetDevice) {
        Socket clientSocket = getSocketFromConnectionRequests(targetDevice);
        if (clientSocket != null) {
            try {
                clientSocket.close();
                System.out.println("connection declined successfully.");
            } catch (IOException e) {

            }
        }
    }
}

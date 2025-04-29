package controller;

import types.Device;
import types.DeviceConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
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
        try (Socket clientSocket = new Socket(targetDevice.getAddress(), targetDevice.getTcpPort())) {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Waiting for the target device to accept the connection...");
            clientSocket.setSoTimeout(5000); // wait for 5 seconds before timing out
            if (!(in.readLine().equals("ACK"))) {
                System.out.println("Target device declined the connection.");
                return null;
            }
            return clientSocket;
        } catch (SocketTimeoutException e) {
            System.out.println("connection to target device timed out.");
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    // for receiver
    private Socket getAvailableSocketByDevice(Device targetDevice) {
        Socket clientSocket = null;
        synchronized (connectionRequestQueue) {
            for (DeviceConnection curr : connectionRequestQueue) {
                if (curr.device.equals(targetDevice)) {
                    clientSocket = curr.clientSocket;
                    break;
                }

            }
            if (clientSocket == null) {
                System.out.println("available socket of this device not found");
                return null;
            }
            if (clientSocket.isClosed()) {
                System.out.println("the connection to the device has already closed.");
                return null;
            }
            return clientSocket;
        }
    }



    public Socket acceptConnection(Device targetDevice) {
        Socket clientSocket = getAvailableSocketByDevice(targetDevice);
        if (clientSocket != null)
            System.out.println("Connected to device: " + targetDevice.getHostname() + ", IP: " + targetDevice.getAddress());
        return clientSocket;
    }

    public void declineConnection(Device targetDevice) {
        Socket clientSocket = getAvailableSocketByDevice(targetDevice);
        if (clientSocket != null) {
            try {
                clientSocket.close();
                System.out.println("connection declined successfully.");
            } catch (IOException e) {

            }
        }
    }
}

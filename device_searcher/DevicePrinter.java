package device_searcher;

import types.BackgroundTask;
import types.BackgroundTaskStatic;
import types.Device;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

public class DevicePrinter extends BackgroundTask {

    private final DatagramSocket socket;
    private final List<Device> availableDevices;
    private final String deviceInfoPrefix;

    public DevicePrinter(DatagramSocket socket, List<Device> availableDevices, String deviceInfoPrefix) {
        this.socket = socket;
        this.availableDevices = availableDevices;
        this.deviceInfoPrefix = deviceInfoPrefix;
    }

    public void kill() {
        socket.close();
        terminate();
    }

    @Override
    public void run() {
        try {
            socket.setSoTimeout(5000); // Wait for 5 seconds for responses
            byte[] receiveData = new byte[1024];

            while (!isTerminated()) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String deviceInfo = new String(receivePacket.getData(), 0, receivePacket.getLength());
                if (deviceInfo.startsWith(deviceInfoPrefix)) {
                    try {
                        Device device = new Device(deviceInfo);
                        int i;
                        synchronized (availableDevices) {
                            i = availableDevices.size();
                            availableDevices.add(device);
                        }
//                        if (i == 0) {
//                            System.out.println();
//                        }
                        System.out.println(i + ". " + device.getHostname() + "(IP=" + device.getAddress().toString() +")");
                    } catch (UnknownHostException e) {
                        System.out.println("Unknown host: " + deviceInfo);
                    } catch (NumberFormatException e) {
                        System.out.println("port number should be a number: " + deviceInfo);
                    } catch (Exception e) {
                        System.out.println("Invalid device info received: " + deviceInfo);
                    }
                }
            }
        } catch (SocketTimeoutException e) {
        } catch (IOException e) {
        }
        if (!isTerminated()) {
            synchronized (availableDevices) {
                if (availableDevices.isEmpty()) {
                    System.out.println("No active local devices found. (input 'r' to search again, 'n' to quit searching)");
                } else {
                    System.out.println("would you like to connect to any of these devices? (if yes, input their index (e.g. '3'), if no, input 'r' to search again, or 'n' to quit searching.)");
                }
            }
        }
//        System.out.println("Device printer terminated");
    }
}

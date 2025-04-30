package device_searcher;

import types.Device;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class DeviceSearcher {
    private static String broadcastMessage;
    private static String deviceInfoPrefix;

    public static void init(String b, String d) {
        broadcastMessage = b;
        deviceInfoPrefix = d;
    }

    public static List<Device> searchForDevices(int udpPort) {
        List<Device> devices = new ArrayList<>();
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            String queryMessage = broadcastMessage;
            byte[] sendData = queryMessage.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                    InetAddress.getByName("255.255.255.255"), udpPort);
            socket.send(sendPacket);
            System.out.println("Searching for devices...");

            socket.setSoTimeout(5000); // Wait for 5 seconds for responses
            byte[] receiveData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String deviceInfo = new String(receivePacket.getData(), 0, receivePacket.getLength());
                if (deviceInfo.startsWith(deviceInfoPrefix)) {
                    try {
                        Device device = new Device(deviceInfo);
                        int i = devices.size();
                        devices.add(device);
//                        if (i == 0) {
//                            System.out.println();
//                        }
                        System.out.println(i + ". " + device.getHostname() + "(IP=" + device.getAddress() +")");
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
            return devices;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

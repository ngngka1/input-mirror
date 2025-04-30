package device_searcher;

import types.Device;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class DeviceSearcher {
    private static String broadcastMessage;
    private static String deviceInfoPrefix;
    private final List<Device> availableDevices;
    private DevicePrinter activeDevicesPrinter;

    public DeviceSearcher(String b, String d) {
        broadcastMessage = b;
        deviceInfoPrefix = d;
        availableDevices = new ArrayList<>();
        activeDevicesPrinter = null;
    }

    public void initAvailableDevices() {
        availableDevices.clear();
    }

    public List<Device> getAvailableDevices() {
        return availableDevices;
    }

    public void stopDevicePrinter() {
        if (activeDevicesPrinter != null) {
            activeDevicesPrinter.kill();
            activeDevicesPrinter = null;
        }
    }

    public void printLocalDevicesNonBlocking(int udpPort) {
        stopDevicePrinter();
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            String queryMessage = broadcastMessage;
            byte[] sendData = queryMessage.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                    InetAddress.getByName("255.255.255.255"), udpPort);
            socket.send(sendPacket);
            System.out.println("Searching for devices...");
            activeDevicesPrinter = new DevicePrinter(socket, availableDevices, deviceInfoPrefix);
            new Thread(activeDevicesPrinter).start();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("e:" + e);
        }
    }
}

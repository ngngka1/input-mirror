package device_searcher;

import types.BackgroundTaskStatic;
import utils.CloseableInterrupter;

import java.io.*;
import java.net.*;


// listens to UDP broadcasts from other active local devices, which then provides them with ip information about this device
public class BroadcastHandler extends BackgroundTaskStatic {
    private static BroadcastHandler runningInstance = null;

    private final int udpPort; // which port this device can accept TCP connection
    private final int tcpPort; // which port this device can accept TCP connection
    private final String broadcastMessage;
    private final String deviceInfoPrefix;

    public static void init(int udpPort, int tcpPort, String broadcastMessage, String deviceInfoPrefix) {
        if (runningInstance == null) {
            runningInstance = new BroadcastHandler(udpPort, tcpPort, broadcastMessage, deviceInfoPrefix);
            new Thread(runningInstance).start();
        } else {
            System.out.println("There can only be at most one running BroadcastHandler");
        }
    }

    private String buildBroadcastResponseMessage(String hostname, String hostAddress, int tcpPort) {
        return deviceInfoPrefix +
                ":" +
                hostname +
                ":" +
                hostAddress +
                ":" +
                tcpPort;
    }

    public BroadcastHandler(int udpPort, int tcpPort, String broadcastMessage, String deviceInfoPrefix)
    {
        this.udpPort = udpPort;
        this.tcpPort = tcpPort;
        this.broadcastMessage = broadcastMessage;
        this.deviceInfoPrefix = deviceInfoPrefix;
    }

    public void respond(DatagramSocket socket, InetAddress senderAddress, int senderPort)
    {
        try {
            String deviceInfoBuilder = buildBroadcastResponseMessage(InetAddress.getLocalHost().getHostName(), InetAddress.getLocalHost().getHostAddress(), tcpPort);
            byte[] sendData = (deviceInfoBuilder).getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, senderAddress, senderPort);
            socket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(udpPort);
            CloseableInterrupter.hook(socket);
            byte[] receiveData = new byte[1024];

            while (!isTerminated()) {
                if (isPaused()) {continue;}
                DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivedPacket);
                String message = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
                if (message.equals(broadcastMessage)) {
                    InetAddress receivedFrom = receivedPacket.getAddress();
//                    System.out.println("Received a broadcast message from: " + receivedFrom); // for debug
                    if (!receivedFrom.equals(InetAddress.getLocalHost())) {
                        respond(socket, receivedFrom, receivedPacket.getPort());
                    } else {
//                        System.out.println("Received a broadcast message from itself: " + receivedFrom); // for debug
                    }
                }
            }

//            System.out.println("BroadcastHandler terminated"); // for debug
        } catch (SocketException e) {
            e.printStackTrace();
//            System.out.println("BroadcastHandler terminated"); // for debug
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

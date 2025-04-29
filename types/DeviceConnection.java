package types;

import java.net.Socket;
import java.util.HashMap;

public class DeviceConnection {
    public Socket clientSocket;
    public Device device;

    public DeviceConnection(Socket clientSocket, Device device) {
        this.clientSocket = clientSocket;
        this.device = device;
    }
}
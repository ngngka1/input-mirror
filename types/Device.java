package types;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Device {
    private final String hostname;
    private final InetAddress address;
    private final int tcpPort;

    public Device(String hostname, InetAddress address, int tcpPort)
    {
        this.hostname = hostname;
        this.address = address;
        this.tcpPort = tcpPort;
    }

    public Device(String deviceInfoString) throws UnknownHostException, NumberFormatException, IndexOutOfBoundsException {
        String[] info = deviceInfoString.split(":");
        this.hostname = info[1];
        this.address = InetAddress.getByName(info[2]);
        this.tcpPort = Integer.parseInt(info[3]);
    }

    public String getHostname() {return hostname;}
    public InetAddress getAddress() {return address;}
    public int getTcpPort() {return tcpPort;}

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Device)) {
            return false;
        }
        return ((Device) obj).getAddress() == this.address && ((Device) obj).getTcpPort() == this.tcpPort;
    }
}

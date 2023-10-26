package comms;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class UdpSender {
    private int port;
    private String address;

    public UdpSender(int port, String address) throws UnknownHostException {
        this.port = port;
        this.address = address;

    }

    public void send(byte[] messageBuffer, boolean broadcast)
            throws IllegalArgumentException, SocketException, IOException {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(broadcast);
            DatagramPacket packet = new DatagramPacket(messageBuffer, messageBuffer.length,
                    InetAddress.getByName(this.address), this.port);
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
            System.out.println("error is here!!");
            System.err.println(e.getMessage());
        } finally {
            if (socket != null)
                socket.close();
        }
    }
}

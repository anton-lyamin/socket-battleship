package comms;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class UdpSender {

    private InetAddress address;
    private int port;

    public UdpSender(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public void send(byte[] messageBuffer, boolean broadcast)
            throws IllegalArgumentException, SocketException, IOException {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(broadcast);
            DatagramPacket packet = new DatagramPacket(messageBuffer, messageBuffer.length,
                    this.address, this.port);
            socket.send(packet);
            socket.close();
        } catch (SocketException e) {
            throw new SocketException("UDP Error: Could not open udp server socket");
        } catch (IOException e) {
            throw new IOException("UDP Error: Could not send packet on port " + this.port);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("UDP Error: Invalid port number or buffer length " + this.port);
        } finally {
            if (socket != null)
                socket.close();
        }
    }
}

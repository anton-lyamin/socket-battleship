package comms;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class UdpListener {
    private int port;

    public UdpListener(int port) {
        this.port = port;
    }

    public DatagramPacket listen(int timeout, byte[] buffer)
            throws IllegalArgumentException, SocketException, IOException {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(this.port);
            socket.setSoTimeout(5000);
            socket.receive(packet);

            return packet;
        } catch (SocketTimeoutException e) {
            return null;
        } catch (SocketException e) {
            throw new SocketException("UDP Error: Could not open udp socket on port " + this.port);
        } catch (IOException e) {
            throw new IOException("UDP Error: Could not receive packet on port " + this.port);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("UDP Error: Invalid port number " + this.port);

        } finally {
            System.out.println("closing socket");
            if (socket != null)
                socket.close();
        }
    }

}

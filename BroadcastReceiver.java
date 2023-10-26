import java.net.*;

public class BroadcastReceiver {
    public static void main(String[] args) throws Exception {
        int port = 12345;
        DatagramSocket socket = new DatagramSocket(port);

        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        // Listen for broadcast messages for 30 seconds
        long startTime = System.currentTimeMillis();
        long duration = 30 * 1000; // 30 seconds

        while (System.currentTimeMillis() - startTime < duration) {
            socket.receive(receivePacket);
            String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received message: " + message);
        }

        socket.close();
    }
}
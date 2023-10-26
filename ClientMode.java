import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientMode {
    private DatagramSocket broadcastSocket;
    private DatagramPacket receivePacket;

    public ClientMode(DatagramSocket broadcastSocket, DatagramPacket receivePacket) {
        this.broadcastSocket = broadcastSocket;
        this.receivePacket = receivePacket;
    }

    public String listen() {
        System.out.println("Listening for broadcast messages");
        try {
            broadcastSocket.setSoTimeout(5000);
            broadcastSocket.receive(receivePacket);
            String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
        } catch (SocketTimeoutException e) {
            System.out.println("No game found. Starting new game.");
            broadcastSocket.setBroadcast(true);

            byte[] sendData = message.getBytes();
            DatagramPacket packet = new DatagramPacket(sendData, sendData.length,
                    InetAddress.getByName(broadcastAddress), 1234);
            broadcastSocket.send(packet);
            broadcastSocket.close();

            System.out.println("Waiting for other player to join...");
            try {
                ServerSocket serverSocket = new ServerSocket(tcpPort);
                broadcastSocket.setSoTimeout(5000);
                serverSocket.accept();
            }
        }
    }
}

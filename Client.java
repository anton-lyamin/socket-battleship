import java.net.*;

public class Client {
    public static void main(String[] args) throws Exception {
        String broadcastAddress = args[0];
        int broadcastPort = 1234;
        if (args[1] != null) {
            broadcastPort = Integer.parseInt(args[1]);
        }
        int tcpPort = generateRandomPort();
        String message = "NEW GAME" + ":" + tcpPort + ":" + "10";

        DatagramSocket socket = new DatagramSocket(broadcastPort);

        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        // Listen for broadcast messages for 30 seconds
        long startTime = System.currentTimeMillis();
        long duration = 30 * 1000; // 30 seconds

        System.out.println("Listening for broadcast messages");
        try {
            socket.setSoTimeout(5000);
            socket.receive(receivePacket);
            message = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received message: " + message);

            String[] messageParts = message.split(":");
            String tcpConnectionPort = messageParts[1];

            Socket clientSocket = new Socket(receivePacket.getAddress(), Integer.parseInt(tcpConnectionPort));
            System.out.println("Connected to server on port " + tcpConnectionPort);

        } catch (SocketTimeoutException e) {
            System.out.println("No game found. Starting new game.");
            socket.setBroadcast(true);

            byte[] sendData = message.getBytes();
            DatagramPacket packet = new DatagramPacket(sendData, sendData.length,
                    InetAddress.getByName(broadcastAddress), 1234);
            socket.send(packet);
            socket.close();

            System.out.println("Waiting for other player to join...");
            try {
                ServerSocket serverSocket = new ServerSocket(tcpPort);
                socket.setSoTimeout(5000);
                serverSocket.accept();
            }
        }
    }

    public static int generateValidTcpPort() {
        return (int) (Math.random() * 100 + 9000);
    }

    public void create

}
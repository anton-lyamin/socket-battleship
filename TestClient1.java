import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

import comms.TcpClientSocket;
import comms.TcpServerSocket;
import comms.UdpListener;
import comms.UdpSender;
import game.GridType;

public class TestClient1 {

    public static void main(String[] args) {
        int broadcastPort = 5000;
        int listenPort = 6000;
        int gamePort = 1234;
        String broadcastAddress = "127.0.0.1";
        Scanner scanner = new Scanner(System.in);

        byte[] buffer = new byte[128];
        DatagramPacket packet = null;
        boolean tcpConnection = false;
        try {
            UdpListener listener = new UdpListener(listenPort);
            System.out.println("Listening on port " + listenPort);
            packet = listener.listen(5000, buffer);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }

        if (packet != null) {
            String message = new String(packet.getData());
            System.out.println("Received message: " + message);
            String[] messageParts = message.split(":");

            if (messageParts[0].equals("NEW GAME")) {
                System.out.println("Connecting to game on port " + messageParts[1]);
                int port = Integer.parseInt(messageParts[1]);
                InetAddress address = packet.getAddress();
                Connector connector = null;
                try {
                    TcpClientSocket client = new TcpClientSocket(address, port);
                    client.connect();
                    PrintWriter out = client.getOut();
                    BufferedReader in = client.getIn();
                    System.out.println("hereeee");
                    connector = new Connector(GridType.OPPONENT, out, in, scanner);
                    connector.startGame(10);
                    // out.println("Hello client 2");

                    // String response = in.readLine();
                    // System.out.println("Received response: " + response);

                    client.close();
                    tcpConnection = true;
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }

        if (!tcpConnection) {
            System.out.println("No broadcast received");
            Connector connector = null;
            try {
                System.out.println("broadcasting on port " + broadcastPort);
                buffer = String.format("NEW GAME:%d:%d", gamePort, 10).getBytes();
                UdpSender sender = new UdpSender(broadcastPort, broadcastAddress);
                sender.send(buffer, true);

                TcpServerSocket server = new TcpServerSocket(gamePort);
                server.accept(5000);
                PrintWriter out = server.getOut();
                BufferedReader in = server.getIn();
                connector = new Connector(GridType.PLAYER, out, in, scanner);
                connector.startGame(10);
                // String inputLine = in.readLine();
                // System.out.println("Received message: " + inputLine);
                // out.println("Welcome Client 2");
                server.close();

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

    }
}

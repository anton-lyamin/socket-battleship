package comms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.xml.crypto.Data;

import util.Pair;

public class CommunicationSystem {
    private InetAddress broadcastAddress;
    private int broadcastPort;
    private int listenPort;
    private boolean isServer;
    private int boardSize;

    public CommunicationSystem(InetAddress broadcastAddress, int broadcastPort, int listenPort) {
        this.broadcastAddress = broadcastAddress;
        this.broadcastPort = broadcastPort;
        this.listenPort = listenPort;
        this.isServer = false;
        this.boardSize = 0;
    }

    public boolean isServer() {
        return this.isServer;
    }

    public int getBoardSize() {
        return this.boardSize;
    }

    public Pair<PrintWriter, BufferedReader> setupConnection(int boardSize, int gamePort) throws Exception {
        Pair<PrintWriter, BufferedReader> connection = null;
        try {
            connection = connectAsClient();
        } catch (Exception e) {
            System.out.println("Could not connect as client due to Exception:");
            System.out.println(e.getMessage());

            connection = connectAsServer(boardSize, gamePort);
            return connection;
        }

        if (connection.getFirst() == null || connection.getSecond() == null)
            connection = connectAsServer(boardSize, gamePort);

        System.out.println("connection = " + connection);
        return connection;
    }

    public Pair<PrintWriter, BufferedReader> connectAsClient()
            throws SocketException, IOException, IllegalArgumentException, Exception {
        byte[] buffer = new byte[128];
        DatagramPacket packet = null;

        System.out.println("Listening on port " + this.listenPort);
        UdpListener listener = new UdpListener(this.listenPort);
        packet = listener.listen(5000, buffer);

        if (packet == null)
            return new Pair<PrintWriter, BufferedReader>(null, null);

        // TODO: write newgame class and converter to avoid doing here
        String message = new String(packet.getData());
        String[] messageParts = message.split(":");
        int gamePort = Integer.parseInt(messageParts[1]);
        this.boardSize = Integer.parseInt(messageParts[2].trim());
        InetAddress serverAddress = packet.getAddress();

        // TODO: Could change these exceptions to be handled as setting up a tcp
        // connection could be reattempted isntead of immediate app shutdown
        System.out.println("Connecting to game on port " + gamePort);
        TcpClientSocket client = new TcpClientSocket(serverAddress, gamePort);
        client.connect();
        PrintWriter out = client.getOut();
        BufferedReader in = client.getIn();
        this.isServer = false;

        return new Pair<PrintWriter, BufferedReader>(out, in);
    }

    public Pair<PrintWriter, BufferedReader> connectAsServer(int boardSize, int gamePort)
            throws SocketException, IOException, IllegalArgumentException, Exception {
        System.out.println("broadcasting on port " + broadcastPort);

        byte[] buffer = String.format("NEW GAME:%d:%d", gamePort, boardSize).getBytes();
        UdpSender sender = new UdpSender(this.broadcastAddress, this.broadcastPort);
        sender.send(buffer, true);

        System.out.println("Waiting for connection");
        TcpServerSocket server = new TcpServerSocket(gamePort);
        server.accept(5000);

        System.out.println("Connection accepted");
        PrintWriter out = server.getOut();
        BufferedReader in = server.getIn();
        this.isServer = true;

        return new Pair<PrintWriter, BufferedReader>(out, in);
    }

}

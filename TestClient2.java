import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;

import comms.TcpClientSocket;
import comms.TcpServerSocket;
import comms.UdpListener;
import comms.UdpSender;
import game.GameEngine;
import game.GridType;
import util.Pair;

public class TestClient2 {

    public static void main(String[] args) {
        int broadcastPort = 6000;
        int listenPort = 5000;
        int gamePort = 1234;
        String address = "127.0.0.1";
        Scanner scanner = new Scanner(System.in);
        InetAddress formattedAddress;
        try {
            formattedAddress = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            System.out.println("Error: Could not format address due to Exception:");
            return;
        }

        System.out.println("Enter board size:");
        String boardSize = scanner.nextLine();
        int boardSizeInt;
        try {
            boardSizeInt = Integer.parseInt(boardSize.trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: Could not parse board size.");
            scanner.close();
            return;
        }

        CommunicationSystem commSystem = new CommunicationSystem(formattedAddress, broadcastPort, listenPort);

        Pair<PrintWriter, BufferedReader> comms = new Pair<PrintWriter, BufferedReader>(null, null);
        PrintWriter out = comms.getFirst();
        BufferedReader in = comms.getSecond();

        try {
            while (out == null || in == null) {
                if (out != null)
                    out.close();

                if (in != null)
                    in.close();

                comms = commSystem.setupConnection(boardSizeInt, gamePort);
                out = comms.getFirst();
                in = comms.getSecond();
            }

        } catch (Exception e) {
            System.out.println("Error: Could not setup connection due to Exception:");
            System.out.println(e.getMessage());
            scanner.close();
            return;
        }

        GridType whoStarts = (commSystem.isServer()) ? GridType.PLAYER : GridType.OPPONENT;
        GameEngine gameEngine = new GameEngine();

        if (commSystem.getBoardSize() != 0)
            boardSizeInt = commSystem.getBoardSize();

        try {
            gameEngine.newGame(boardSizeInt, whoStarts);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            scanner.close();
            return;
        }

        MoveDirector moveDirector = new MoveDirector(gameEngine, out, in, scanner);
        moveDirector.makeMove();

        scanner.close();
    }
}

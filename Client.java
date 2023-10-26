import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import comms.CommunicationSystem;
import game.GameEngine;
import game.GridType;
import game.MoveDirector;
import util.Pair;

public class Client {

    public static void main(String[] args) {
        String broadcastAddress;
        int broadcastPort;
        int gamePort = randomGamePort(9000, 100);
        InetAddress formattedAddress;

        if (args.length != 2) {
            System.out.println("Error: Invalid number of arguments");
        }
        broadcastAddress = args[0];
        try {
            broadcastPort = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Error: Could not parse broadcast port.");
            return;
        }
        try {
            formattedAddress = InetAddress.getByName(broadcastAddress);
        } catch (UnknownHostException e) {
            System.out.println("Error: Bad address, chose a valid address");
            return;
        }

        Scanner scanner = new Scanner(System.in);
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

        CommunicationSystem commSystem = new CommunicationSystem(formattedAddress, broadcastPort, broadcastPort);

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

    public static int randomGamePort(int start, int range) {
        int gamePort = (int) (Math.random() * range) + start;
        return gamePort;
    }
}

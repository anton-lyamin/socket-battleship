import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import game.Coordinate;
import game.GameDisplay;
import game.GameEngine;
import game.GameStatus;
import game.GridType;
import game.MoveOutcome;

public class MoveDirector {
    private GameEngine gameEngine;
    private PrintWriter out;
    private BufferedReader in;
    private GameDisplay ui;
    private Scanner scanner;
    private MoveOutcome moveOutcome;

    public MoveDirector(GameEngine gameEngine, PrintWriter out, BufferedReader in, Scanner scanner) {
        this.gameEngine = gameEngine;
        this.ui = new GameDisplay();
        this.out = out;
        this.in = in;
        this.scanner = scanner;
    }

    public void makeMove() {
        GridType whosTurn = this.gameEngine.getCurrentTurn();
        boolean error = false;
        System.out.println(ui.displayTurn(gameEngine.getGrid(), gameEngine.getOpponentGrid(),
                gameEngine.getPlayerShipsLeft(), gameEngine.getOpponentShipsLeft(), gameEngine.getPlayerShips(),
                gameEngine.getOpponentShips(),
                gameEngine.getTurnCount(),
                whosTurn, this.moveOutcome));
        while (this.gameEngine.getStatus() != GameStatus.COMPLETED) {
            try {
                if (whosTurn == GridType.PLAYER) {
                    error = this.sendAttack();
                } else {
                    error = this.takeAttack();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Shutting down");
                out.println("ERROR");
            }

            if (error) {
                System.out.println("Error: opponent sent error");
                System.out.println("Shutting down");
                break;
            }
            whosTurn = this.gameEngine.getCurrentTurn();
            System.out.println(ui.displayTurn(gameEngine.getGrid(), gameEngine.getOpponentGrid(),
                    gameEngine.getPlayerShipsLeft(), gameEngine.getOpponentShipsLeft(), gameEngine.getPlayerShips(),
                    gameEngine.getOpponentShips(),
                    gameEngine.getTurnCount(),
                    whosTurn, this.moveOutcome));

        }
    }

    public boolean takeAttack() throws IllegalArgumentException, IOException {
        String attack;
        try {
            attack = in.readLine();
        } catch (IOException e) {
            throw new IOException("Error: encountered issue reading opponent response");
        }
        Coordinate coordinate = FireConverter.parse(attack, this.gameEngine.getGridSize());

        if (coordinate == null) {
            out.println("ERROR");
            return true;
        }
        this.moveOutcome = this.gameEngine.takeFire(coordinate);
        String message = MoveOutcomeConverter.print(this.moveOutcome);
        out.println(message);
        return false;
    }

    public boolean sendAttack() throws IOException, IllegalArgumentException {
        Coordinate playerMove = this.getPlayerMove();
        out.println(String.format("FIRE:%s-%d", playerMove.getRowAsChar(), playerMove.getCol()));
        String response;
        try {
            response = in.readLine();
        } catch (IOException e) {
            throw new IOException("Error: encountered issue reading opponent response");
        }
        this.moveOutcome = MoveOutcomeConverter.parse(response, this.gameEngine.getGridSize());
        if (this.moveOutcome == null) {
            out.println("ERROR");
            return true;
        }
        this.gameEngine.updateOpponentGrid(this.moveOutcome);
        return false;
    }

    public Coordinate getPlayerMove() throws IllegalArgumentException {
        System.out.println("Enter your move: ");
        String input = this.scanner.nextLine();
        System.out.println("caught " + input);
        return new Coordinate(input.substring(0, 1), input.substring(1), this.gameEngine.getGridSize());
    }
}

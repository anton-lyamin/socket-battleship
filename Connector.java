import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Scanner;

import game.Coordinate;
import game.GameDisplay;
import game.GameEngine;
import game.GameStatus;
import game.GridType;
import game.MoveOutcome;

/**
 * Connector
 */
public class Connector {
    GridType whoStarts;
    GameEngine gameEngine;
    GameController gameController;
    GameDisplay gameDisplay;
    PrintWriter out;
    BufferedReader in;
    Scanner scanner;

    public Connector(GridType whoStart, PrintWriter out, BufferedReader in, Scanner scanner) {
        this.whoStarts = whoStart;
        this.gameEngine = new GameEngine();
        this.gameController = new GameController(this.gameEngine);
        this.gameDisplay = new GameDisplay();
        this.out = out;
        this.in = in;
        this.scanner = scanner;
    }

    public void startGame(int gridSize) {
        try {
            this.gameEngine.newGame(gridSize, this.whoStarts);
            this.playGame();
        } catch (Exception e) {
            out.println("ERROR");
        }
    }

    public void playGame() {
        boolean gameOver = false;
        boolean error = false;

        if (this.whoStarts == GridType.PLAYER) {
            System.out.println(gameDisplay.displayTurn(gameEngine.getGrid(), gameEngine.getOpponentGrid(),
                    gameEngine.getPlayerShipsLeft(), gameEngine.getOpponentShipsLeft(), gameEngine.getTurnCount(),
                    GridType.PLAYER, null, null));
            while (!gameOver && !error) {
                try {
                    if (this.gameEngine.getStatus() == GameStatus.COMPLETED) {
                        gameOver = true;
                        continue;
                    }
                    Coordinate playerMove = this.getPlayerMove();
                    this.gameEngine.isCoordinateLegal(playerMove);
                    out.println(String.format("FIRE:%s-%d", playerMove.getRowAsChar(), playerMove.getCol()));

                    String move = in.readLine();
                    String[] moveParts = move.split(":");
                    MoveOutcome outcome = this.gameController.processOpponentMessage(moveParts);

                    System.out.println(gameDisplay.displayTurn(gameEngine.getGrid(), gameEngine.getOpponentGrid(),
                            gameEngine.getPlayerShipsLeft(), gameEngine.getOpponentShipsLeft(),
                            gameEngine.getTurnCount(),
                            GridType.OPPONENT, outcome, outcome.getCoordinate()));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    out.println("ERROR");
                    error = true;
                }
                try {
                    if (this.gameEngine.getStatus() == GameStatus.COMPLETED) {
                        gameOver = true;
                        continue;
                    }
                    String move = in.readLine();
                    String[] moveParts = move.split(":");
                    MoveOutcome outcome = this.gameController.processOpponentMessage(moveParts);

                    String message = MoveOutcomeConverter.print(outcome);
                    out.println(message);
                    System.out.println(gameDisplay.displayTurn(gameEngine.getGrid(), gameEngine.getOpponentGrid(),
                            gameEngine.getPlayerShipsLeft(), gameEngine.getOpponentShipsLeft(),
                            gameEngine.getTurnCount(),
                            GridType.PLAYER, outcome, outcome.getCoordinate()));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    out.println("ERROR");
                    error = true;
                }
            }
        } else {
            System.out.println(gameDisplay.displayTurn(gameEngine.getGrid(), gameEngine.getOpponentGrid(),
                    gameEngine.getPlayerShipsLeft(), gameEngine.getOpponentShipsLeft(), gameEngine.getTurnCount(),
                    GridType.OPPONENT, null, null));
            while (!gameOver && !error) {
                try {
                    if (this.gameEngine.getStatus() == GameStatus.COMPLETED) {
                        gameOver = true;
                        continue;
                    }
                    String move = in.readLine();
                    String[] moveParts = move.split(":");
                    MoveOutcome outcome = this.gameController.processOpponentMessage(moveParts);
                    // TODO: Error should be caught earlier, outcome should return null only if
                    // internal game error
                    // also thered no check for game over. If
                    if (outcome == null) {
                        error = true;
                        continue;
                    }
                    String message = MoveOutcomeConverter.print(outcome);
                    out.println(message);

                    System.out.println(gameDisplay.displayTurn(gameEngine.getGrid(), gameEngine.getOpponentGrid(),
                            gameEngine.getPlayerShipsLeft(), gameEngine.getOpponentShipsLeft(),
                            gameEngine.getTurnCount(),
                            GridType.PLAYER, outcome, outcome.getCoordinate()));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    out.println("ERROR");
                    error = true;
                }

                try {
                    if (this.gameEngine.getStatus() == GameStatus.COMPLETED) {
                        gameOver = true;
                        continue;
                    }
                    Coordinate playerMove = this.getPlayerMove();
                    this.gameEngine.isCoordinateLegal(playerMove);
                    out.println(String.format("FIRE:%s-%d", playerMove.getRowAsChar(), playerMove.getCol()));

                    String move = in.readLine();
                    String[] moveParts = move.split(":");
                    MoveOutcome outcome = this.gameController.processOpponentMessage(moveParts);

                    System.out.println(gameDisplay.displayTurn(gameEngine.getGrid(), gameEngine.getOpponentGrid(),
                            gameEngine.getPlayerShipsLeft(), gameEngine.getOpponentShipsLeft(),
                            gameEngine.getTurnCount(),
                            GridType.OPPONENT, outcome, outcome.getCoordinate()));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    out.println("ERROR");
                    error = true;
                }
            }
        }
    }

    public Coordinate getPlayerMove() {
        System.out.println("Enter your move: ");
        String input = this.scanner.nextLine();
        System.out.println("caught " + input);
        return new Coordinate(input.substring(0, 1), input.substring(1));
    }

}
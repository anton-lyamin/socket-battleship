import java.util.Scanner;

import game.Coordinate;
import game.GameDisplay;
import game.GameEngine;
import game.GameStatus;
import game.Grid;
import game.GridType;
import game.MoveOutcome;
import game.OutcomeType;

public class Test {
    public static void main(String[] args) {
        GameEngine playerEngine = new GameEngine();
        GameEngine opponentEngine = new GameEngine();
        playerEngine.newGame(10, GridType.PLAYER, opponentEngine);
        opponentEngine.newGame(10, GridType.OPPONENT, playerEngine);

        GameDisplay gameDisplay = new GameDisplay();

        System.out.println(gameDisplay.displayTurn(playerEngine.getGrid(), opponentEngine.getGrid(),
                playerEngine.getPlayerShipsLeft(), playerEngine.getOpponentShipsLeft(), playerEngine.getTurnCount(),
                GridType.PLAYER, null, null));

        boolean gameOver = false;
        while (!gameOver) {
            if (playerEngine.getCurrentTurn() == GridType.OPPONENT) {
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                }
                int row = (int) (Math.random() * 10);
                int col = (int) (Math.random() * 10);
                MoveOutcome outcome = opponentEngine.fire(row, col);

                System.out.println(gameDisplay.displayTurn(playerEngine.getGrid(), opponentEngine.getGrid(),
                        playerEngine.getPlayerShipsLeft(), playerEngine.getOpponentShipsLeft(),
                        playerEngine.getTurnCount(),
                        GridType.PLAYER, outcome, new Coordinate(row + "", col + "", 10)));

                if (outcome.getOutcome() == OutcomeType.GAME_OVER)
                    gameOver = true;

                continue;
            }

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter coordinate (i.e A1): ");
            String move = scanner.nextLine();
            Coordinate coordinate = new Coordinate(move.charAt(0) + "", move.charAt(1) + "", 10);
            System.out.println("coordinate chosed: " + coordinate.getRow() + " " + coordinate.getCol());
            MoveOutcome outcome = playerEngine.fire(coordinate.getRow(), coordinate.getCol());

            System.out.println(gameDisplay.displayTurn(playerEngine.getGrid(), opponentEngine.getGrid(),
                    playerEngine.getPlayerShipsLeft(), playerEngine.getOpponentShipsLeft(), playerEngine.getTurnCount(),
                    playerEngine.getCurrentTurn(), outcome, coordinate));

            if (playerEngine.getStatus() == GameStatus.COMPLETED) {
                gameOver = true;
            }
        }
    }

}

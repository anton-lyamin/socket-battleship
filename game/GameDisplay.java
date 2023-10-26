package game;

public class GameDisplay {

    public String displayTurn(Grid playerGrid, Grid opponentGrid, int playerShipsLeft, int opponentShipsLeft,
            int turnNumber, GridType whosTurn,
            MoveOutcome outcome, Coordinate lastMove) {
        String oppGrid = "OPPONENT:\n" + this.displayGrid(opponentGrid);
        String oppShipsLeft = "Ships Left: " + opponentShipsLeft;
        String plyrGrid = "PLAYER:\n" + this.displayGrid(playerGrid);
        String plyrShipsLeft = "Ships Left: " + playerShipsLeft;
        String separator = "---------------------------------------------";
        String turn = (whosTurn == GridType.PLAYER) ? "YOUR TURN" : "ENEMY's TURN";
        String turnString = "TURN " + turnNumber + ": " + turn;

        String result = oppGrid + "\n" + oppShipsLeft + "\n\n" + plyrGrid + "\n" + plyrShipsLeft + "\n"
                + separator + "\n" + turnString + "\n" + separator + "\n";

        if (outcome != null) {
            String who = (whosTurn != GridType.PLAYER) ? "you" : "enemy";

            String action = (outcome.getOutcome() == OutcomeType.MISS) ? "MISSED" : "HIT";
            String outcomeString = "Last Move: " + who + " " + action + " " + "cell" + "[" + lastMove.getRowAsChar()
                    + ":"
                    + lastMove.getCol() + "]";

            result += outcomeString + "\n";

            String gameOver = "GAME OVER";

            if (outcome.getOutcome() == OutcomeType.SUNK)
                result += "SUNK " + outcome.getShip().toString() + "\n";

            if (outcome.getOutcome() == OutcomeType.GAME_OVER)
                result += gameOver + "\n";
        }

        return result;
    }

    public String displayGrid(Grid grid) {
        int size = grid.getSize();

        String gridString = "    +";
        for (int k = 0; k < size; k++) {
            gridString += "---+";
        }
        gridString += "\n";
        gridString += "    |";
        for (int i = 0; i < size; i++) {
            gridString += " " + (i + 1) + " |";
        }
        gridString += "\n";
        gridString += "+---+";
        for (int i = 0; i < size; i++) {
            gridString += "---+";
        }
        gridString += "\n";
        for (int i = 0; i < size; i++) {
            gridString += "| " + (char) (i + 65) + " |";
            for (int k = 0; k < size; k++) {
                gridString += " " + this.getCellString(grid, i, k) + " |";
            }
            gridString += "\n";
            gridString += "+---+";
            for (int k = 0; k < size; k++) {
                gridString += "---+";
            }
            gridString += "\n";
        }
        return gridString;
    }

    public String getCellString(Grid grid, int row, int col) {
        CellStatus status = grid.getCell(row, col).getStatus();
        Ship ship = grid.getCell(row, col).getShip();

        if (status == CellStatus.MISS && ship == null)
            return ".";

        if (status == CellStatus.HIT)
            return "X";

        if (grid.getType() == GridType.PLAYER && ship != null)
            switch (ship.getType()) {
                case CARRIER:
                    return "A";
                case BATTLESHIP:
                    return "B";
                case CRUISER:
                    return "C";
                case SUBMARINE:
                    return "S";
                case PATROL:
                    return "P";
            }

        return " ";
    }
}

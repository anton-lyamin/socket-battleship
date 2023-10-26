package game;

import java.util.ArrayList;
import java.util.List;

public class Coordinate {
    private int row;
    private int col;

    public Coordinate(String row, String col) throws IllegalArgumentException {
        validate(row, col);
        this.row = row.charAt(0) - 65;
        this.col = Integer.parseInt(col);
    }

    public static void validate(String row, String col) throws IllegalArgumentException {
        char rowChar = row.charAt(0);

        if (rowChar < 'A' || rowChar > 'Z')
            throw new IllegalArgumentException("Error: row is not a valid letter");

        try {
            int colNum = Integer.parseInt(col);
            if (colNum < 1 || colNum > 26)
                throw new IllegalArgumentException("Error: column is not a valid number");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error: column is not a number");
        }
    }

    public int getRow() {
        return this.row;
    }

    public char getRowAsChar() {
        return (char) (this.row + 65);
    }

    public int getCol() {
        return this.col;
    }

    public int getColZeroIndexed() {
        return this.col - 1;
    }
}

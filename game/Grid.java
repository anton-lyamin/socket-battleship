package game;

public class Grid {
    private Cell grid[][];
    private int size;
    private final GridType type;

    public Grid(int size, GridType type) {
        this.size = size;
        this.type = type;
        grid = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                grid[i][k] = new Cell();
            }
        }
    }

    public int getSize() {
        return size;
    }

    public GridType getType() {
        return type;
    }

    public Cell getCell(int row, int col) throws IllegalArgumentException {
        if (row < 0 || row >= size || col < 0 || col >= size)
            throw new IllegalArgumentException("Row or column must be within the grid");

        return grid[row][col];
    }
}

package game;

public class GameEngine {
    private Grid playerGrid;
    private Grid opponentGrid;
    private Ship[] playerShips = new Ship[5];
    private Ship[] opponentShips = new Ship[5];
    private GridType currentTurn = GridType.PLAYER;
    private GameStatus status = GameStatus.NO_GAME;
    private int turnCount = 0;
    private int gridSize;

    public GameEngine() {
    }

    public void newGame(int gridSize, GridType whosGoesFirst)
            throws IllegalArgumentException {
        if (gridSize < 10 || gridSize > 26)
            throw new IllegalArgumentException("Grid size must be between 10 and 26");

        this.playerGrid = new Grid(gridSize, GridType.PLAYER);
        this.opponentGrid = new Grid(gridSize, GridType.OPPONENT);
        this.createShips(this.playerShips);
        this.createShips(this.opponentShips);
        this.createShipLayout(this.playerGrid, this.playerShips);
        this.status = GameStatus.IN_PROGRESS;
        this.currentTurn = whosGoesFirst;
        this.gridSize = gridSize;
    }

    private void createShips(Ship[] ships) {
        ShipType[] shipTypes = ShipType.values();
        for (int i = 0; i < shipTypes.length; i++) {
            ships[i] = new Ship(shipTypes[i]);
        }
    }

    private void createShipLayout(Grid grid, Ship[] ships) {
        for (Ship ship : ships) {
            boolean shipPlaced = false;
            while (!shipPlaced) {
                shipPlaced = placeShip(grid, ship);
            }
        }
    }

    private boolean placeShip(Grid grid, Ship ship) {
        int gridSize = grid.getSize();
        int shipLength = ship.getType().getLength();

        int startLengthAccounted = (int) (Math.random() * (gridSize - shipLength));
        int start = (int) (Math.random() * gridSize);
        int direction = (int) (Math.random() * 2);
        int row = (direction == 0) ? startLengthAccounted : start;
        int col = (direction == 0) ? start : startLengthAccounted;

        if (!canShipBePlaced(grid, ship, row, col, direction))
            return false;

        Cell cell;
        for (int i = 0; i < shipLength; i++) {
            cell = grid.getCell(row, col);
            cell.setShip(ship);
            if (direction == 0)
                row++;
            else
                col++;
        }

        return true;
    }

    private boolean canShipBePlaced(Grid grid, Ship ship, int row, int col, int direction) {
        int shipLength = ship.getType().getLength();
        Cell cell;
        for (int i = 0; i < shipLength; i++) {
            cell = grid.getCell(row, col);
            if (cell.hasShip())
                return false;
            if (direction == 0)
                row++;
            else
                col++;
        }
        return true;
    }

    public Grid getGrid() {
        return this.playerGrid;
    }

    public void updateOpponentGrid(MoveOutcome outcome)
            throws IllegalStateException, IllegalArgumentException {
        if (this.status != GameStatus.IN_PROGRESS)
            throw new IllegalStateException("Game is not in progress");
        if (this.currentTurn != GridType.PLAYER)
            throw new IllegalStateException("It is not the player's turn");

        int row = outcome.getCoordinate().getRow();
        int col = outcome.getCoordinate().getColZeroIndexed();

        Cell cell = opponentGrid.getCell(row, col);
        switch (outcome.getOutcome()) {
            case MISS:
                cell.setStatus(CellStatus.MISS);
                break;
            case HIT:
                cell.setStatus(CellStatus.HIT);
                break;
            case SUNK:
                cell.setStatus(CellStatus.HIT);
                this.sinkOpponentShip(outcome.getShip());
                break;
            case GAME_OVER:
                cell.setStatus(CellStatus.HIT);
                this.sinkOpponentShip(outcome.getShip());
                this.status = GameStatus.COMPLETED;
                break;
        }
        this.currentTurn = GridType.OPPONENT;
        this.turnCount++;
    }

    public MoveOutcome takeFire(Coordinate coordinate) throws IllegalArgumentException, IllegalStateException {
        if (this.status != GameStatus.IN_PROGRESS)
            throw new IllegalStateException("Game is not in progress");

        if (this.currentTurn != GridType.OPPONENT)
            throw new IllegalStateException("It is not the opponent's turn");

        this.currentTurn = GridType.PLAYER;
        this.turnCount++;

        int row = coordinate.getRow();
        int col = coordinate.getColZeroIndexed();
        Ship ship;
        Cell cell = this.playerGrid.getCell(row, col);

        if (!cell.hasShip()) {
            cell.setStatus(CellStatus.MISS);
            return new MoveOutcome(coordinate, OutcomeType.MISS, null);
        }

        ship = cell.getShip();
        ship.hit();
        cell.setStatus(CellStatus.HIT);

        if (!ship.isSunk())
            return new MoveOutcome(coordinate, OutcomeType.HIT, null);

        if (!this.isGameOver()) {
            return new MoveOutcome(coordinate, OutcomeType.SUNK, ship.getType());
        }
        this.status = GameStatus.COMPLETED;
        return new MoveOutcome(coordinate, OutcomeType.GAME_OVER, ship.getType());
    }

    private void sinkOpponentShip(ShipType type) {
        for (Ship ship : opponentShips) {
            if (ship.getType() == type) {
                ship.sink();
                return;
            }
        }
    }

    private boolean isGameOver() {
        for (Ship ship : playerShips) {
            if (!ship.isSunk())
                return false;
        }
        return true;
    }

    public GameStatus getStatus() {
        return this.status;
    }

    public GridType getCurrentTurn() {
        return this.currentTurn;
    }

    public int getTurnCount() {
        return this.turnCount;
    }

    public Grid getOpponentGrid() {
        return this.opponentGrid;
    }

    private int getShipsLeft(GridType gridType) {
        Ship[] ships = (gridType == GridType.PLAYER) ? this.playerShips : this.opponentShips;
        int shipsLeft = 0;
        for (Ship ship : ships) {
            if (!ship.isSunk())
                shipsLeft++;
        }
        return shipsLeft;
    }

    public int getPlayerShipsLeft() {
        return this.getShipsLeft(GridType.PLAYER);
    }

    public int getOpponentShipsLeft() {
        return this.getShipsLeft(GridType.OPPONENT);
    }

    public int getGridSize() {
        return this.gridSize;
    }

    public Ship[] getOpponentShips() {
        return this.opponentShips;
    }

    public Ship[] getPlayerShips() {
        return this.playerShips;
    }
}

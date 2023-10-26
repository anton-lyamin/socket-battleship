package game;

public class Cell {
    private Ship ship = null;
    private CellStatus status = CellStatus.UNKNOWN;

    public Cell() {
    }

    public CellStatus getStatus() {
        return status;
    }

    public void setStatus(CellStatus status) {
        this.status = status;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public boolean hasShip() {
        return ship != null;
    }

    // TODO: take out user interface related code into different class.
    public String toString() {
        if (status == CellStatus.MISS)
            return ".";

        if (status == CellStatus.HIT)
            return "X";

        if (ship != null)
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

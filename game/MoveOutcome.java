package game;

public class MoveOutcome {
    Coordinate coordinate;
    private OutcomeType outcome;
    private ShipType ship;

    public MoveOutcome(Coordinate coordinate, OutcomeType outcome, ShipType ship) {
        if (coordinate == null)
            throw new IllegalArgumentException("Coordinate must be provided");
        if (outcome == OutcomeType.SUNK && ship == null)
            throw new IllegalArgumentException("Ship must be provided for SUNK outcome");
        if (outcome == OutcomeType.GAME_OVER && ship == null)
            throw new IllegalArgumentException("Ship must be provided for GAME OVER outcome");
        if ((outcome != OutcomeType.SUNK && outcome != OutcomeType.GAME_OVER) && ship != null)
            throw new IllegalArgumentException("Ship must not be provided for non-SUNK outcome");

        this.coordinate = coordinate;
        this.outcome = outcome;
        this.ship = ship;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public OutcomeType getOutcome() {
        return outcome;
    }

    public ShipType getShip() {
        return ship;
    }
}

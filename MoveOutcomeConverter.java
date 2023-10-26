import game.Coordinate;
import game.MoveOutcome;
import game.OutcomeType;
import game.ShipType;

public class MoveOutcomeConverter {
    public static String print(MoveOutcome moveOutcome) {
        if (moveOutcome == null)
            throw new IllegalArgumentException("MoveOutcome must be provided");
        if (moveOutcome.getOutcome() == null || moveOutcome.getCoordinate() == null)
            throw new IllegalArgumentException("MoveOutcome must have an outcome and coordinate");

        String outcomeString = moveOutcome.getOutcome().getStringValue();
        String row = "" + moveOutcome.getCoordinate().getRowAsChar();
        int col = moveOutcome.getCoordinate().getCol();
        ShipType ship = moveOutcome.getShip();
        String result = String.format("%s:%s-%d", outcomeString, row, col);

        if (ship != null)
            result += ":" + ship.getStringValue();

        return result;
    }

    public static MoveOutcome parse(String move, int gridSize) throws IllegalArgumentException {
        String[] moveParts = move.split(":");
        if ("ERROR".equals(moveParts[0]))
            return null;
        OutcomeType outcomeType = getOutcomeFromString(moveParts[0]);
        checkMessageLength(outcomeType, moveParts.length);
        String[] coordinateParts = moveParts[1].split("-");
        Coordinate coordinate = new Coordinate(coordinateParts[0], coordinateParts[1], gridSize);
        ShipType ship = null;
        if (moveParts.length > 2)
            ship = getShipTypeFromString(moveParts[2]);

        return new MoveOutcome(coordinate, outcomeType, ship);
    }

    public static void checkMessageLength(OutcomeType outcomeType, int length) {
        if (outcomeType == OutcomeType.HIT && length != 2)
            throw new IllegalArgumentException("HIT message must have 2 parts");
        if (outcomeType == OutcomeType.MISS && length != 2)
            throw new IllegalArgumentException("MISS message must have 2 parts");
        if (outcomeType == OutcomeType.SUNK && length != 3)
            throw new IllegalArgumentException("SUNK message must have 3 parts");
        if (outcomeType == OutcomeType.GAME_OVER && length != 3)
            throw new IllegalArgumentException("GAME OVER message must have 3 parts");
    }

    public static OutcomeType getOutcomeFromString(String outcomeString) {
        if (outcomeString == null)
            throw new IllegalArgumentException("Outcome must be provided");

        switch (outcomeString) {
            case "HIT":
                return OutcomeType.HIT;
            case "MISS":
                return OutcomeType.MISS;
            case "SUNK":
                return OutcomeType.SUNK;
            case "GAME END":
                return OutcomeType.GAME_OVER;
            default:
                throw new IllegalArgumentException("Outcome must be one of HIT, MISS, SUNK, GAME END");
        }
    }

    public static ShipType getShipTypeFromString(String shipString) {
        for (ShipType ship : ShipType.values()) {
            if (ship.getStringValue().equals(shipString))
                return ship;
        }
        throw new IllegalArgumentException("Ship must be one of CARRIER, BATTLESHIP, DESTROYER, SUBMARINE, PATROL");

    }
}

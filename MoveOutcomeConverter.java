import game.MoveOutcome;
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
}

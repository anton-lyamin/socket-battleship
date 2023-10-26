package util;

import game.Coordinate;

public class FireConverter {
    public static Coordinate parse(String move, int gridSize) throws IllegalArgumentException {
        String[] moveParts = move.split(":");
        if ("ERROR".equals(moveParts[0]))
            return null;
        String[] coordinateParts = moveParts[1].split("-");
        return new Coordinate(coordinateParts[0], coordinateParts[1], gridSize);
    }
}

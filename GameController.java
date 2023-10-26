import game.Coordinate;
import game.GameEngine;
import game.MoveOutcome;
import game.OutcomeType;
import game.ShipType;

public class GameController {
    private final GameEngine gameEngine;

    public GameController(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public MoveOutcome processOpponentMessage(String[] message) {
        String outcome = message[0];
        String[] coords;
        Coordinate coordinate;
        MoveOutcome moveOutcome;

        if ("FIRE".equals(message[0])) {
            if (message.length != 2)
                throw new IllegalArgumentException("Error: invalid message length");
            coords = message[1].split("-");
            coordinate = new Coordinate(coords[0], coords[1]);
            return gameEngine.takeFire(coordinate);
        }

        if ("MISS".equals(outcome)) {
            if (!missValid(message))
                return null;
            coords = message[1].split("-");
            coordinate = new Coordinate(coords[0], coords[1]);
            moveOutcome = new MoveOutcome(coordinate, OutcomeType.MISS, null);
            gameEngine.updateOpponentGrid(moveOutcome);

            return moveOutcome;
        }
        if ("HIT".equals(outcome)) {
            if (!hitValid(message))
                return null;
            coords = message[1].split("-");
            coordinate = new Coordinate(coords[0], coords[1]);
            moveOutcome = new MoveOutcome(coordinate, OutcomeType.HIT, null);
            gameEngine.updateOpponentGrid(moveOutcome);

            return moveOutcome;
        }
        if ("SUNK".equals(outcome)) {
            if (!sunkValid(message))
                return null;
            coords = message[1].split("-");
            coordinate = new Coordinate(coords[0], coords[1]);
            ShipType ship = getShip(message[2]);
            moveOutcome = new MoveOutcome(coordinate, OutcomeType.SUNK, ship);
            gameEngine.updateOpponentGrid(moveOutcome);

            return moveOutcome;
        }
        if ("GAME END".equals(outcome)) {
            if (!gameEndValid(message))
                return null;
            coords = message[1].split("-");
            coordinate = new Coordinate(coords[0], coords[1]);
            ShipType ship = getShip(message[2]);
            moveOutcome = new MoveOutcome(coordinate, OutcomeType.GAME_OVER, ship);
            gameEngine.updateOpponentGrid(moveOutcome);

            return moveOutcome;
        }
        if ("ERROR".equals(outcome)) {
            return null;
            // shut down the game
            // return command to shut down app
            // or maybe just catch this in the calling function and use as shutdown
            // condition
        }
        return null;
    }

    public boolean missValid(String[] message) {
        if (message.length != 2) {
            return false;
        }
        String[] coordinates = message[1].split("-");
        try {
            Coordinate.validate(coordinates[0], coordinates[1]);
            return true;

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean hitValid(String[] message) {
        if (message.length != 2) {
            return false;
        }
        String[] coordinates = message[1].split("-");
        try {
            Coordinate.validate(coordinates[0], coordinates[1]);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean sunkValid(String[] message) {
        if (message.length != 3) {
            return false;
        }
        String[] coordinates = message[1].split("-");
        try {
            Coordinate.validate(coordinates[0], coordinates[1]);
            return shipExists(message[2]);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean gameEndValid(String[] message) {
        if (message.length != 3) {
            return false;
        }
        String[] coordinates = message[1].split("-");
        try {
            Coordinate.validate(coordinates[0], coordinates[1]);
            return shipExists(message[2]);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean shipExists(String shipname) {
        for (ShipType ship : ShipType.values()) {
            if (ship.getStringValue().equals(shipname)) {
                return true;
            }
        }
        return false;
    }

    public ShipType getShip(String shipname) {
        for (ShipType ship : ShipType.values()) {
            if (ship.getStringValue().equals(shipname)) {
                return ship;
            }
        }
        return null;
    }
}

package game;

public enum ShipType {
    CARRIER(5, "Carrier"),
    BATTLESHIP(4, "Battleship"),
    CRUISER(3, "Cruiser"),
    SUBMARINE(3, "Submarine"),
    PATROL(2, "Patrol");

    private int length;
    private String stringValue;

    ShipType(int length, String stringValue) {
        this.length = length;
        this.stringValue = stringValue;
    }

    public int getLength() {
        return length;
    }

    public String getStringValue() {
        return stringValue;
    }
}

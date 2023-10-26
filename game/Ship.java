package game;

public class Ship {
    private ShipType type;
    private int length;
    private int hits = 0;

    public Ship(ShipType type) {
        this.type = type;
        this.length = type.getLength();
    }

    public ShipType getType() {
        return type;
    }

    public boolean isSunk() {
        return this.hits == this.length;
    }

    public void sink() {
        this.hits = this.length;
    }

    public void hit() {
        this.hits++;
    }

}

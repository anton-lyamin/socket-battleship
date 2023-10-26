package game;

public enum OutcomeType {
    HIT("HIT"),
    MISS("MISS"),
    SUNK("SUNK"),
    GAME_OVER("GAME END");

    private String stringValue;

    OutcomeType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}

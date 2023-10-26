package game;

public enum OutcomeType {
    HIT("HIT"),
    MISS("MISS"),
    SUNK("SUNK"),
    GAME_OVER("GAME END");

    // TODO: should probably change enum to GAME_END for consistency
    private String stringValue;

    OutcomeType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}

package checkers.capstone.game;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Space {
    Empty(false, false, 0),
    Red(true, false, 1),
    Black(false, false, 2),
    RedKing(true, true, 3),
    BlackKing(false, true, 4);

    public final boolean isRed;
    public final boolean isKing;
    private final int value;

    private Space(boolean isRed, boolean isKing, int value){
        this.isRed = isRed;
        this.isKing = isKing;
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    public boolean isItRed(){
        return isRed;
    }

    public boolean isItBlack(){
        return !isRed;
    }
}

package checkers.capstone.game;

import com.fasterxml.jackson.annotation.JsonValue;

/**The status on whether a game is ongoing or the end result of it. */
public enum GameStatus {
    Ongoing(0),
    Draw(1),
    PlayerOneWon(2),
    PlayerTwoWon(3);

    private final int value;

    GameStatus(int value){
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}

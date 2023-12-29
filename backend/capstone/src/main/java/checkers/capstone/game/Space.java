package checkers.capstone.game;

public enum Space {
    Empty(false, false),
    Red(true, false),
    Black(false, false),
    RedKing(true, true),
    BlackKing(false, true);

    public final boolean isRed;
    public final boolean isKing;

    private Space(boolean isRed, boolean isKing){
        this.isRed = isRed;
        this.isKing = isKing;
    }
}

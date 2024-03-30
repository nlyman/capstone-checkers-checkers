package checkers.capstone.dtos;

import checkers.capstone.game.GameStatus;

public class BoardRoomDTO {
    private final long id;
    private final String otherPlayerName;
    private final GameStatus status;
    private final boolean youPlayerOne;

    /**
     * @param id The id of the board.
     * @param otherPlayerName The player the one who asked for the data is against.
     * @param status The status of the game.
     */
    public BoardRoomDTO(long id, String otherPlayerName, GameStatus status, boolean youPlayerOne) {
        this.id = id;
        this.otherPlayerName = otherPlayerName;
        this.status = status;
        this.youPlayerOne = youPlayerOne;
    }

    public long getId() {
        return id;
    }

    public String getOtherPlayerName() {
        return otherPlayerName;
    }

    public GameStatus getStatus() {
        return status;
    }

    public boolean isYouPlayerOne() {
        return youPlayerOne;
    }
}

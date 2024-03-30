package checkers.capstone.dtos;

import checkers.capstone.game.Board;
import checkers.capstone.game.GameStatus;
import checkers.capstone.game.Row;
import checkers.capstone.game.Space;


public class BoardDTO {
    private final boolean playerOneTurn;
    private final boolean youPlayerOne;
    private final String player1Name;
    private final String player2Name;
    private final Space board[][];
    private final GameStatus status;
    private final short turn;

    public BoardDTO(boolean playerOneTurn, boolean youPlayerOne, String player1Name, String player2Name, Board board, GameStatus status, short turn) {
        this.playerOneTurn = playerOneTurn;
        this.youPlayerOne = youPlayerOne;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.board = setBoard(board);
        this.status = status;
        this.turn = turn;
    }

    private Space[][] setBoard(Board board){
        Space[][] result = new Space[8][4];
        for(short i=0; i<8; i++){
            Row row = board.getRow(i);
            for(short j=0; j<4; j++){
                result[i][j]=row.getColumn(j);
            }
        }
        return result;
    }

    public boolean isPlayerOneTurn() {
        return playerOneTurn;
    }

    public boolean isYouPlayerOne() {
        return youPlayerOne;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public Space[][] getBoard() {
        return board;
    }

    public GameStatus getStatus() {
        return status;
    }

    public short getTurn() {
        return turn;
    }
}

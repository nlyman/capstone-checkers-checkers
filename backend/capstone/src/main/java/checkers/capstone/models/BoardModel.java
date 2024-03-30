package checkers.capstone.models;

import java.util.Collection;

import checkers.capstone.game.Board;
import checkers.capstone.game.GameStatus;
import jakarta.persistence.*;

@Entity
public class BoardModel extends BaseModel{
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player1_id", nullable = false)
    private UserModel player1;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player2_id", nullable = false)
    private UserModel player2;

    @Column(nullable = false)
    private boolean playerOneTurn;

    @Column(name = "player1_id", insertable = false, updatable = false, nullable = false)
    private Long player1Id;

    @Column(name = "player2_id", insertable = false, updatable = false, nullable = false)
    private Long player2Id;

    @Embedded
    @Column(nullable = false)
    private Board board;

    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL)
    private Collection<MoveModel> moves;

    private GameStatus status;

    public BoardModel() { }

    public BoardModel(UserModel player1, UserModel player2) {
        this.player1 = player1;
        this.player1Id = player1.getId();
        this.player2 = player2;
        this.player2Id = player2.getId();

        this.playerOneTurn = true;
        this.board = new Board();

        this.status = GameStatus.Ongoing;
    }

    public UserModel getPlayer1() {
        return player1;
    }

    public void setPlayer1(UserModel player1) {
        this.player1 = player1;
    }

    public UserModel getPlayer2() {
        return player2;
    }

    public void setPlayer2(UserModel player2) {
        this.player2 = player2;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean isPlayerOneTurn() {
        return playerOneTurn;
    }

    public void setPlayerOneTurn(boolean playerOneTurn) {
        this.playerOneTurn = playerOneTurn;
    }

    public Long getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(Long player1Id) {
        this.player1Id = player1Id;
    }

    public Long getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(Long player2Id) {
        this.player2Id = player2Id;
    }

    public Collection<MoveModel> getMoves() {
        return moves;
    }

    public void setMoves(Collection<MoveModel> moves) {
        this.moves = moves;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }
}

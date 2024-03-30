package checkers.capstone.models;

import checkers.capstone.game.rules.ChangeSpace;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class MoveModel extends BaseModel{

    @ManyToOne
    private BoardModel board;

    @ElementCollection
    private ChangeSpace[] changes;

    public MoveModel() { }

    public MoveModel(BoardModel board, ChangeSpace[] changes) {
        this.board = board;
        this.changes = changes;
    }

    public ChangeSpace[] getChanges() {
        return changes;
    }

    public void setChanges(ChangeSpace[] changes) {
        this.changes = changes;
    }

    public BoardModel getBoard() {
        return board;
    }

    public void setBoard(BoardModel board) {
        this.board = board;
    }
}

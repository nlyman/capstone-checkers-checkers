package checkers.capstone.game.rules;

import checkers.capstone.game.Space;
import jakarta.persistence.Embeddable;

@Embeddable
public class ChangeSpace {
    public Space previous;
    public Space current;
    public short column;
    public short row;

    
    public ChangeSpace() { }

    /**
     * @param previous The piece present before change.
     * @param current The piece present after change.
     * @param column The column being changed.
     * @param row The row being changed.
     */
    public ChangeSpace(Space previous, Space current, short column, short row) {
        this.previous = previous;
        this.current = current;
        this.column = column;
        this.row = row;
    }
}

package checkers.capstone.game.rules;

import checkers.capstone.game.Space;
import jakarta.persistence.Embeddable;

@Embeddable
public class ChangeSpace {
    public final Space previous;
    public final Space current;
    public final short column;
    public final short row;

    public ChangeSpace(Space previous, Space current, short column, short row) {
        this.previous = previous;
        this.current = current;
        this.column = column;
        this.row = row;
    }
}

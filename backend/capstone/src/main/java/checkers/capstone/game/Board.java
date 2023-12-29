package checkers.capstone.game;

import checkers.capstone.game.rules.ChangeSpace;
import jakarta.persistence.*;

@Embeddable
public class Board {
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "column0", column = @Column(name = "0spot0")),
        @AttributeOverride(name = "column1", column = @Column(name = "0spot1")),
        @AttributeOverride(name = "column2", column = @Column(name = "0spot2")),
        @AttributeOverride(name = "column3", column = @Column(name = "0spot3"))
    })
    private Row row0;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "column0", column = @Column(name = "1spot0")),
        @AttributeOverride(name = "column1", column = @Column(name = "1spot1")),
        @AttributeOverride(name = "column2", column = @Column(name = "1spot2")),
        @AttributeOverride(name = "column3", column = @Column(name = "1spot3"))
    })
    private Row row1;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "column0", column = @Column(name = "2spot0")),
        @AttributeOverride(name = "column1", column = @Column(name = "2spot1")),
        @AttributeOverride(name = "column2", column = @Column(name = "2spot2")),
        @AttributeOverride(name = "column3", column = @Column(name = "2spot3"))
    })
    private Row row2;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "column0", column = @Column(name = "3spot0")),
        @AttributeOverride(name = "column1", column = @Column(name = "3spot1")),
        @AttributeOverride(name = "column2", column = @Column(name = "3spot2")),
        @AttributeOverride(name = "column3", column = @Column(name = "3spot3"))
    })
    private Row row3;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "column0", column = @Column(name = "4spot0")),
        @AttributeOverride(name = "column1", column = @Column(name = "4spot1")),
        @AttributeOverride(name = "column2", column = @Column(name = "4spot2")),
        @AttributeOverride(name = "column3", column = @Column(name = "4spot3"))
    })
    private Row row4;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "column0", column = @Column(name = "5spot0")),
        @AttributeOverride(name = "column1", column = @Column(name = "5spot1")),
        @AttributeOverride(name = "column2", column = @Column(name = "5spot2")),
        @AttributeOverride(name = "column3", column = @Column(name = "5spot3"))
    })
    private Row row5;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "column0", column = @Column(name = "6spot0")),
        @AttributeOverride(name = "column1", column = @Column(name = "6spot1")),
        @AttributeOverride(name = "column2", column = @Column(name = "6spot2")),
        @AttributeOverride(name = "column3", column = @Column(name = "6spot3"))
    })
    private Row row6;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "column0", column = @Column(name = "7spot0")),
        @AttributeOverride(name = "column1", column = @Column(name = "7spot1")),
        @AttributeOverride(name = "column2", column = @Column(name = "7spot2")),
        @AttributeOverride(name = "column3", column = @Column(name = "7spot3"))
    })
    private Row row7;

    public Board() {
        this.row0 = new Row(Space.Black);
        this.row1 = new Row(Space.Black);
        this.row2 = new Row(Space.Black);
        this.row3 = new Row(Space.Empty);
        this.row4 = new Row(Space.Empty);
        this.row5 = new Row(Space.Red);
        this.row6 = new Row(Space.Red);
        this.row7 = new Row(Space.Red);
    }

    public Board(Row row0, Row row1, Row row2, Row row3, Row row4, Row row5, Row row6, Row row7) {
        this.row0 = row0;
        this.row1 = row1;
        this.row2 = row2;
        this.row3 = row3;
        this.row4 = row4;
        this.row5 = row5;
        this.row6 = row6;
        this.row7 = row7;
    }

    public Row getRow(short i){
        switch(i){
            case 0:
                return row0;
            case 1:
                return row1;
            case 2:
                return row2;
            case 3:
                return row3;
            case 4:
                return row4;
            case 5:
                return row5;
            case 6:
                return row6;
            case 7:
                return row7;
            default:
                throw new Error("That column does not exist.");
        }
    }

    public Space getSpace(short column, short row){
        return getRow(row).getColumn(column);
    }

    public void setSpace(short column, short row, Space space){
        getRow(row).setSpace(column, space);
    }

    public void addMove(ChangeSpace[] changes){
        for(ChangeSpace change : changes)
            setSpace(change.column, change.row, change.current);
    }
}

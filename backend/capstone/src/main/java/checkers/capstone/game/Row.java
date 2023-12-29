package checkers.capstone.game;

import jakarta.persistence.Embeddable;

@Embeddable
public class Row {
    
    private Space column0;
    
    private Space column1;
    
    private Space column2;
    
    private Space column3;

    public Row() { }

    public Row(Space column){
        this.column0 = column;
        this.column1 = column;
        this.column2 = column;
        this.column3 = column;
    }

    public Space getColumn(short i){
        switch(i){
            case 0:
                return column0;
            case 1:
                return column1;
            case 2:
                return column2;
            case 3:
                return column3;
            default:
                throw new Error("attempted to access Column that does not exist.");
        }
    }

    public void setSpace(short i, Space space){
        switch(i){
            case 0:
                column0=space;
                break;
            case 1:
                column1=space;
                break;
            case 2:
                column2=space;
                break;
            case 3:
                column3=space;
                break;
            default:
                throw new Error("attempted to access Column that does not exist.");
        }
    }
}

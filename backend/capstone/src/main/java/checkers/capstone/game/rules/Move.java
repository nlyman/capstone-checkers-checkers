package checkers.capstone.game.rules;

import checkers.capstone.dtos.MoveDTO;

//Note: For the sake of accurate testing only use greater than or less than comparisons/%2== comparisons.
public class Move {

    /**Current column */
    public final short currentColumn;
    /**Current row */
    public final short currentRow;
    /**Target column */
    public final short newColumn;
    /**Target row */
    public final short newRow;

    /**Distance of columns */
    public final short columnDistance;
    /**Distance of rows */
    public final short rowDistance;

    /**Iif move is up*/
    public final boolean isDown;

    /**If move is left */
    public final boolean isLeft;

    public Move(short index, MoveDTO dto){
        this(dto.getColumns(index), dto.getRows(index), dto.getColumns((short)(index+1)), dto.getRows((short)(index+1)));
    }

    public Move(int currentColumn, int currentRow, int newColumn, int newRow){
        this((short)currentColumn, (short)currentRow, (short)newColumn, (short)newRow);
    }

    public Move(short currentColumn, short currentRow, short newColumn, short newRow) {
        this.currentColumn = currentColumn;
        this.currentRow = currentRow;
        this.newColumn = newColumn;
        this.newRow = newRow;
        if (newColumn>currentColumn){
            this.columnDistance=(short)(newColumn-currentColumn);
            isDown=true;
        }
        else{
            this.columnDistance=(short)(currentColumn-newColumn);
            isDown=false;
        }

        if (currentRow>newRow){
            isLeft=true;
        }
        else if(newRow>currentRow){
            isLeft=false;
        }
        else{
            //Minor Note: the illegal move of two columns no rows moved can result true here.
            isLeft=currentColumn%2==0;
        }

        short rowDistance;
        if (currentRow>newRow)
            rowDistance=(short)((currentRow-newRow)*2);
        else
            rowDistance=(short)((newRow-currentRow)*2);

        //This adjusts row distance to work with data.
        if (columnDistance%2==1){
            if (isLeft){
                if (currentColumn%2==0)
                    rowDistance++;
                else
                    rowDistance--;
            }
            else{
                if (currentColumn%2==1)
                    rowDistance++;
                else
                    rowDistance--;
            }
        }

        this.rowDistance = rowDistance;
    }

    public static short getColumnDistance(short index, MoveDTO dto){
        return getColumnDistance(dto.getColumns(index), dto.getColumns((short)(index+1)));
    }

    public static short getColumnDistance(short currentColumn, short newColumn){
        if (currentColumn>newColumn)
            return (short)(currentColumn-newColumn);
        else
            return (short)(newColumn-currentColumn);
    }

    public short getColumnIndex(int distance){
        return getColumnIndex((short)distance);
    }

    public short getColumnIndex(short distance){
        if (isDown)
            return (short)(currentColumn+1);
        else
            return (short)(currentColumn-1);
    }

    /**
     * Uses current fields except new column to calculate row index.
     * @param distance The amount of columns moved.
     * @return The row index diagonal to current position.
     */
    public short getRowIndex(int distance){
        return getRowIndex((short)distance);
    }

    /**
     * Uses current fields except new column to calculate row index.
     * @param distance The amount of columns moved.
     * @return The row index diagonal to current position.
     */
    public short getRowIndex(short distance){
        short result=currentRow;

        if (isLeft){
            result-=(distance)/2;
            result-=(currentColumn+distance+1)%2;
        }
        else{
            result+=(distance)/2;
            result+=(currentColumn+distance)%2;
        }

        return result;
    }
}

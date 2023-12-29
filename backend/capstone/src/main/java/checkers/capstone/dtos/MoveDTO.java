package checkers.capstone.dtos;

public class MoveDTO {

    private final short[] column;
    private final short[] row;

    public MoveDTO(short[] column, short[] row) {
        this.column = column;
        this.row = row;
    }

    public short getColumn(short i){
        return column[i];
    }

    public short getRow(short i){
        return row[i];
    }

    public short getLength(){
        return (short)column.length;
    }
}

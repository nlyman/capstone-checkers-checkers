package checkers.capstone.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**The dto for creating a move in checkers. */
public class MoveDTO {

    @JsonProperty("columns")
    private final short[] columns;
    @JsonProperty("rows")
    private final short[] rows;

    /**
     * @param columns Matching column for each change.
     * @param rows Matching row for each change.
     */
    public MoveDTO(short[] columns, short[] rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public short getColumns(short i){
        return columns[i];
    }

    public short getRows(short i){
        return (short)(rows[i]/2);
    }

    public short getLength(){
        return (short)columns.length;
    }
}

const BoardComponent = (dto) => {
    let isPlayerOne=dto.youPlayerOne;
    let spaces=[];
    const setSpaces=(value)=>{spaces=value};
    const board = {
        getSpace,
        setSpace,
        getSpaces,
        isOwned,
        getAllowedMoves,
        getIsPlayerOne,
        convertColumns,
        convertRows
    };

    //Flips the board's view
    let numbersColumn;
    let numbersRow;
    if (isPlayerOne){
        numbersColumn=[0,1,2,3,4,5,6,7];
        numbersRow=[0, 1, 2, 3];
    }
    else{
        numbersColumn=[7,6,5,4,3,2,1,0];
        numbersRow=[3, 2, 1, 0];
    }

    function convertColumns(columns){
        if (isPlayerOne){
            return;
        }
        for(let i=0; i<columns.length; i++){
            columns[i]=numbersColumn[columns[i]];
        }
        return columns;
    }

    function convertRows(rows){
        if (isPlayerOne){
            return;
        }
        for(let i=0; i<rows.length; i++){
            rows[i]=numbersColumn[rows[i]];
        }
        return rows;
    }

    function setBoard () {//TODO: see if assignment needed.

        let fields = new Array(8);
        for(let i=0; i<8; i++){
            let row = new Array(8);
            for(let j=0, k=0; j<8; j++){
                if ((j+i)%2==1){
                    row[j] = dto.board[numbersColumn[i]][numbersRow[k]];
                    k++;
                }
                else{
                    row[j]=6;
                }
            }
            fields[i] = row;
        }
        setSpaces(fields);
    }

    function getSpace(column, row) {
        return spaces[column][row];
    }

    function setSpace(space, column, row) {
        spaces[column][row]=space;
    }

    function getSpaces(){
        return spaces;
    }

    function isOwned (column, row) {
        switch(getSpace(column, row)){
            case 0:
            case 5:
            case 6:
                return false;
            case 1:
            case 3:
                return isPlayerOne;
            case 2:
            case 4:
                return !isPlayerOne;
        }
    }

    //Will have unintended before if not checked if owned first.
    function getAllowedMoves (firstMove, column, row) {
        let piece = getSpace(column, row);
        let result;
        if (piece>2){//For kings.
            result = new Array(4);
            //Counts down because length is decreased by 1 on failures.
            tryMove(result, 0, firstMove, column, row, 1, 1);
            tryMove(result, result.length-3, firstMove, column, row, -1, 1);
            tryMove(result, result.length-2, firstMove, column, row, 1, -1);
            tryMove(result, result.length-1, firstMove, column, row, -1, -1);
        }
        else{//For not kings.
            result = new Array(2);
            
            //Counts down because length is decreased by 1 on failures.
            tryMove(result, 0, firstMove, column, row, -1, 1);
            tryMove(result, result.length-1, firstMove, column, row, -1, -1);
        }
        return result;
    }

    function tryMove (array, i, firstMove, column, row, yAdjust, xAdjust) {
        column+=yAdjust;
        row+=xAdjust;
        if (checkOffBoard(column, row)){
            array.length--;
            return;
        }
        if (firstMove && getSpace(column, row)==0){
            array[i]=createNormalMove(column, row);
            return;
        }
        if (!tryHopOver(array, i, column, row, yAdjust, xAdjust)){
            array.length--;
        }
    }

    function tryHopOver (array, i, column, row, yAdjust, xAdjust) {
        if (checkOffBoard(column+yAdjust, row+xAdjust)){
            return false;
        }
        switch(getSpace(column, row)){
            case 0:
            case 5:
                return false;
            case 1:
            case 3:
                if (isPlayerOne) return false;
                break;
            case 2:
            case 4:
                if (!isPlayerOne) return false;
                break;
        }
        if (getSpace(column+yAdjust, row+xAdjust)==0){
            array[i]=createHopOverMove(column+yAdjust, row+xAdjust, column, row);
        }
        else{
            return false;
        }
        return true;
    }
    
    function checkOffBoard (column, row) {
        return (column<0 || column>7 || row<0 || row>7);
    }

    function createNormalMove (column, row) {
        return {column: column, row: row};
    }

    function createHopOverMove (column, row, hopY, hopX) {
        return {column: column, row: row, hopY: hopY, hopX: hopX};
    }

    function getIsPlayerOne(){
        return isPlayerOne;
    }

    setBoard();

    return (board);
}

export default BoardComponent;
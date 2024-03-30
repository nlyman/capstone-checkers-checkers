package checkers.capstone.rules.testTools;

import checkers.capstone.game.rules.Move;

public class MoveTestTool {

    public boolean isDown(short i){
        return i%2>=1;
    }

    public boolean isLeft(short i){
        return i%4>=2;
    }

    public boolean skipsOver(short i){
        return i%8>=4;
    }

    public boolean columnIsEven(short i){
        return i%16>=8;
    }

    //%2>=1 it goes down.
    //%4>=2 it goes left.
    //%8>=4 it skips over a piece.
    //%16>=8 currentColumn%2==0.
    public Move generateMove(short i){
        short cC=getCurrentColumn(i);
        short cR=getCurrentRow(i);
        short nC=getNewColumn(i);
        short nR=getNewRow(i);
        return new Move(cC, cR, nC, nR);
    }

    public Move generateMove(boolean isDown, boolean isLeft, boolean skipsOver, boolean columnIsEven){
        short i=0;
        if (isDown)
            i++;
        if (isLeft)
            i+=2;
        if (skipsOver)
            i+=4;
        if (columnIsEven)
            i+=8;
        return generateMove(i);
    }

    public short getCurrentColumn(short i){
        if (columnIsEven(i))
            return 4;
        else
            return 3;
    }

    public short getCurrentRow(short i){
        return 1;
    }

    public short getNewColumn(short i){
        short newColumn=getCurrentColumn(i);
        short distance;
        if (skipsOver(i))
            distance=2;
        else
            distance=1;

        if (isDown(i))
            newColumn+=distance;
        else
            newColumn-=distance;

        return newColumn;
    }

    public short getNewRow(short i){
        short newRow=getCurrentRow(i);
        if (skipsOver(i)){
            if (isLeft(i))
                newRow--;
            else
                newRow++;
        }
        else{
            if (isLeft(i) && !columnIsEven(i)){
                newRow--;
            }
            else if(!isLeft(i) && columnIsEven(i)){
                newRow++;
            }
        }
        return newRow;
    }
}

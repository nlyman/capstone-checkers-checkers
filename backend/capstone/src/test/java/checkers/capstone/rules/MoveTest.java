package checkers.capstone.rules;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import checkers.capstone.game.rules.Move;
import checkers.capstone.rules.testTools.MoveTestTool;

public class MoveTest {

    @Test
    public void testPremadeMoves(){
        MoveTestTool tool = new MoveTestTool();
        Move move;

        for(short i=0; i<16; i++){
            move=tool.generateMove(i);
            assertEquals(tool.getCurrentColumn(i), move.currentColumn);
            assertEquals(tool.getCurrentRow(i), move.currentRow);
            assertEquals(tool.getNewColumn(i), move.newColumn);
            assertEquals(tool.getNewRow(i), move.newRow);

            assertEquals(tool.isDown(i), move.isDown);
            assertEquals(tool.isLeft(i), move.isLeft);
            assertEquals(!tool.skipsOver(i), move.columnDistance==1);
            assertEquals(tool.skipsOver(i), move.columnDistance==2);
            assertEquals(!tool.skipsOver(i), move.rowDistance==1);
            assertEquals(tool.skipsOver(i), move.rowDistance==2);
        }
    }

    @Test
    /**Tests getRowIndex ability to get the space hopped over. */
    public void testGetSpaceHoppedOver(){
        testGetSpaceHoppedOver(false, false, false);
        testGetSpaceHoppedOver(true, false, false);
        testGetSpaceHoppedOver(false, true, false);
        testGetSpaceHoppedOver(false, false, true);
        testGetSpaceHoppedOver(true, true, false);
        testGetSpaceHoppedOver(true, false, true);
        testGetSpaceHoppedOver(false, true, true);
        testGetSpaceHoppedOver(true, true, true);
    }

    private void testGetSpaceHoppedOver(boolean isUp, boolean isLeft, boolean isEven){
        Move move;
        short columnStart;
        short columnLimit;
        short rowStart;
        short rowLimit;
        short columnModifier;
        short rowModifier;
        short resultModifier;

        if (isUp){
            columnStart=2;
            columnLimit=8;
            columnModifier=-2;
        }
        else{
            columnStart=0;
            columnLimit=6;
            columnModifier=2;
        }
        if (!isEven){
            columnStart++;
        }

        if (isLeft){
            rowStart=1;
            rowLimit=4;
            rowModifier=-1;
        }
        else{
            rowStart=0;
            rowLimit=3;
            rowModifier=1;
        }

        if (isLeft && !isEven)
            resultModifier=-1;
        else if(!isLeft && isEven)
            resultModifier=1;
        else
            resultModifier=0;

        for(short i=columnStart; i<columnLimit; i+=2){
            for(short j=rowStart; j<rowLimit; j++){
                move = new Move(i, j, i+columnModifier, j+rowModifier);
                assertEquals((short)(j+resultModifier), move.getRowIndex(1));
            }
        }
    }

    @Test
    /**This thoroughly tests all the possible entries.
    It does this by putting row in a different format. */
    public void testEverythingElse(){
        Move move;

        for(short cC=0; cC<8; cC++){
            for(short nC=0; nC<8; nC++){
                //Adjusts r to match positions in data format move uses.
                for(short cR=(short)((cC+1)%2); cR<8; cR+=2){
                    for(short nR=(short)((nC+1)%2); nR<8; nR+=2){
                        //The data format of move is 0 to 3.
                        move = new Move(cC, cR/2, nC, nR/2);

                        //Makes sure these did not get altered.
                        assertEquals(cC, move.currentColumn);
                        assertEquals(nC, move.newColumn);
                        assertEquals(cR/2, move.currentRow);
                        assertEquals(nR/2, move.newRow);

                        //This tests column distance is correct.
                        short dC;
                        if (cC>nC)
                            dC=(short)(cC-nC);
                        else
                            dC=(short)(nC-cC);
                        assertEquals(dC, move.columnDistance);

                        //This tests row distance is correct.
                        short dR;
                        if (cR>nR)
                            dR=(short)(cR-nR);
                        else
                            dR=(short)(nR-cR);
                        assertEquals(dR, move.rowDistance);

                        //This checks isDown is correct.
                        if (cC>nC)
                            assertEquals(false, move.isDown);
                        else if (nC>cC)
                            assertEquals(true, move.isDown);

                        //This checks isLeft correct.
                        if (cR>nR)
                            assertEquals(true, move.isLeft);
                        else if(nR>cR)
                            assertEquals(false, move.isLeft);
                    }
                }
            }
        }
    }
}

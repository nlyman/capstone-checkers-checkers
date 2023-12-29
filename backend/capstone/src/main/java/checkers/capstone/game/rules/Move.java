package checkers.capstone.game.rules;

public class Move {

    /**Current column */
    public final short cC;
    /**Current row */
    public final short cR;
    /**Target column */
    public final short nC;
    /**Target row */
    public final short nR;

    /**Distance of columns */
    public final short dC;
    /**Distance of rows */
    public final short dR;

    /**Iif move is up*/
    public final boolean isUp;

    /**If move is left */
    public final boolean isLeft;

    public Move(short cC, short cR, short nC, short nR) {
        this.cC = cC;
        this.cR = cR;
        this.nC = nC;
        this.nR = nR;
        if (cC<nC){
            this.dC=(short)(nC-cC);
            isUp=true;
        }
        else{
            this.dC=(short)(cC-nC);
            isUp=false;
        }

        short dR=0;
        if (cR==nR){
            if (cC%2==0)
                this.isLeft=true;
            else
                this.isLeft=false;
        }
        else if(cR<nR){
            this.isLeft=false;
            dR+=nR-cR;
        }
        else{
            this.isLeft=true;
            dR+=nR-cR;
        }

        if (dC%2==1){
            if (cC%2==1){
                if (isLeft){
                    dR++;
                }
            }
            else if (!isLeft){
                dR++;
            }
        }

        this.dR = dR;
    }

    /**
     * Uses current fields except new column to calculate row index.
     * @param distance The amount of columns moved.
     * @return The row index diagonal to current position.
     */
    public short getRowIndex(short distance){
        short result=cR;
        if (isLeft){
            result-=(distance)/2;
            result-=(cC+distance+1)%2;
        }
        else{
            result+=(distance)/2;
            result+=(cC+distance)%2;
        }

        return result;
    }
}

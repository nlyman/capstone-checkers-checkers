package checkers.capstone.game.rules;

import checkers.capstone.dtos.MoveDTO;
import checkers.capstone.game.Space;
import checkers.capstone.models.BoardModel;
import checkers.capstone.models.MoveModel;
import checkers.capstone.repos.custom.UpdateBoard;

public class BaseRules {
    
    protected final BoardModel model;
    private final UpdateBoard repo;

    public BaseRules(BoardModel model, UpdateBoard repo) {
        this.model = model;
        this.repo = repo;
    }

    public final boolean isPlayersTurn(long playerId){
        if (model.isPlayerOneTurn())
            return playerId==model.getPlayer1Id();
        else
            return playerId==model.getPlayer2Id();
    }

    public final boolean isPlayerParticipant(long playerId){
        return playerId==model.getPlayer1Id()
            || playerId==model.getPlayer2Id();
    }

    /**
     * This checks if moving from starting position to new position is valid.
     * @param cC current Column
     * @param cR current Row
     * @param nC new Column
     * @param nR new Row
     * @return If the move succeeded.
     */
    public synchronized boolean attemptMove(long playerId, MoveDTO dto){//Needs more work for rules.

        //Checks if it is the right player here to stop data races.
        if (!isPlayersTurn(playerId))
            return false;

        short length = dto.getLength();
        if (length<2){
            return false;
        }
        ChangeSpace[] changes;

        if(!(length>2)){//If it is a single move.
            Move move = new Move(dto.getColumn((short)0), dto.getRow((short)0), dto.getColumn((short)1), dto.getRow((short)1));
            if (!checkRules(move) || !checkHop(move)){
                return false;
            }
            changes = new ChangeSpace[2];
            //Empty the space the piece started on.
            changes[0] = new ChangeSpace(get(move.cC, move.cR), Space.Empty, move.cC, move.cR);
            //Filling the space the piece moved to.
            changes[1] = new ChangeSpace(Space.Empty, getWithPromote(move), move.nC, move.nR);
            model.getBoard().addMove(changes);
        }
        else{//If it is multiple moves.
            changes = new ChangeSpace[(length-1)*3];

            //Temp is for holding the data of each individual move.
            ChangeSpace[] temp = new ChangeSpace[3];

            //These are for determining the spot being hopped over.
            short midColumn;
            short midRow;

            //A move reads the current spot and the spot after it.  Thus the loop ends one early.
            length--;
            for(short i=0; i<length; i++){
                Move move = new Move(dto.getColumn(i), dto.getRow(i), dto.getColumn((short)(i+1)), dto.getRow((short)(i+1)));
                //Chaining moves requires a piece hopped over every move.
                if (!checkRules(move) || !checkHopOver(move)){
                    return false;
                }

                //Empty the space the piece started on.
                temp[0] = new ChangeSpace(get(move.cC, move.cR), Space.Empty, move.cC, move.cR);
                //Filling the space the piece moved to.
                temp[2] = new ChangeSpace(Space.Empty, getWithPromote(move), move.nC, move.nR);

                midColumn=move.cC;
                if (move.isUp)
                    midColumn--;
                else
                    midColumn++;
                midRow = move.getRowIndex((short)1);
                //Emptying the space the piece hopped over.
                temp[1] = new ChangeSpace(get(midColumn, midRow), Space.Empty, midColumn, midRow);

                model.getBoard().addMove(temp);
                for(short a=0; a<3; a++)
                    changes[(3*i)+a] = temp[a];
            }
        }

        MoveModel newMove = new MoveModel(model, changes);
        repo.changeFields(model.getId(), !model.isPlayerOneTurn(), newMove);
        return true;
    }

    /**
     * This checks the basic rules except ones for it being a valid movement.
     * @param move The move being checked.
     */
    protected boolean checkRules(Move move){
        Space piece=get(move.cC, move.cR);
        if (!isOwned(piece))
            return false;

        if (!onBoard(move.nC, move.nR))
            return false;

        if (!checkDirection(piece, move.isUp))
            return false;

        return true;
    }

    /**The basic method for getting a piece from the board. */
    protected final Space get(short column, short row){
        return model.getBoard().getSpace(row, column);
    }

    /**
     * Checks if the direction being moved is valid for the piece.
     * @param piece The piece being moved.
     * @param isUp Whether the piece is moving up or down in the board.
     */
    protected boolean checkDirection(Space piece, boolean isUp){
        if (piece.isKing)
            return true;

        //Red moves up.
        if (isUp)
            if (piece.isRed)
                return true;

        //Since it did not move up it has to not be red.
        return (!piece.isRed);
    }

    /**
     * Checks if either column or row are not valid entries for the board.
     * @param column 0 to 8
     * @param row 0 to 4
     */
    protected boolean onBoard(short column, short row){
        return (column >=0 && column<8 && row >=0 && row<4);
    }

    /**
     * Checks if a move was valid based on distance and if hopping over pieces.
     */
    protected boolean checkHop(Move move){
        //Can only move to an empty space.
        if (get(move.nC, move.nR)!=Space.Empty)
            return false;

        //A single column moved with no pieces hopped over.
        if (move.dC==1)
            return (move.dR==1);

        //Returns the check if it valid for hoping over a space.
        return checkHopOver(move);
    }

    //The differences are recorded so I don't have to recalculate them.
    //This is a split up of checkHop.
    /**
     * Checks if hopping over pieces was valid.
     * @param dC difference between columns
     * @param dR different between rows.
     * @param cC current Column
     * @param cR current Row
     * @param nC new Column
     * @param nR new Row
     */
    protected boolean checkHopOver(Move move){
        //Hopping over one piece could only move two columns.
        //Them swapping which changes means it balances out to a change of 1.
        if (move.dC!=2 || move.dR!=2)
            return false;

        //The column being hopped over.
        short c=move.cC;
        if (move.nC>move.cC)
            c++;
        else
            c--;

        //The space is it going over.
        Space pad=get(c, move.getRowIndex((short)2));

        //Finally confirms it is neither empty nor an owned piece.
        return (!(pad==Space.Empty || isOwned(pad)));
    }
    
    /**
     * Checks if the piece is owned by current player.
     * @param column The column
     * @param row The row
     */
    protected boolean isOwned(Space space){
        //Player one controls red.
        if (model.isPlayerOneTurn()){
            if (space.isRed)
                return true;
            return false;
        }

        //Since empty is also not red has to check that it is not empty.
        return (space!=Space.Empty && !space.isRed);
    }

    /**
     * This is similar to get but it returns a king if piece met requirements to promote.
     * @param move The move being made.  reads nC and nR.
     * @return The piece that should be in the space.
     */
    protected Space getWithPromote(Move move){
        Space piece = get(move.nC, move.nR);
        if (piece.isKing){
            return piece;
        }

        if (move.isUp){
            if (move.nC==0){
                return Space.RedKing;
            }
        }
        else{
            if (move.nC==7){
                return Space.BlackKing;
            }
        }

        return piece;
    }
}

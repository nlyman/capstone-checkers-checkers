package checkers.capstone.game.rules;

import checkers.capstone.controllers.tools.exception_stuff.BoardResult;
import checkers.capstone.dtos.BoardDTO;
import checkers.capstone.dtos.MoveDTO;
import checkers.capstone.game.Board;
import checkers.capstone.game.Space;
import checkers.capstone.game.rules.RuleInterfaces.TheGame;
import checkers.capstone.models.BoardModel;
import checkers.capstone.models.MoveModel;
import checkers.capstone.repos.custom.UpdateBoard;

public class BaseRules implements TheGame {
    
    protected final BoardModel model;
    protected final UpdateBoard repo;
    private final String name1;
    private final String name2;
    private short viewerCount=1;
    private short turn;

    public BaseRules(BoardModel model, UpdateBoard repo, String name1, String name2) {
        this.model = model;
        this.repo = repo;
        this.name1 = name1;
        this.name2 = name2;
    }

    /**
     * This checks if moving from starting position to new position is valid.
     * @param currentColumn current Column
     * @param currentRow current Row
     * @param newColumn new Column
     * @param newRow new Row
     * @return If the move succeeded.
     */
    public synchronized BoardResult attemptMove(long playerId, MoveDTO dto){//Needs more work for rules.

        //Checks if it is the right player here to stop data races.
        if (!isPlayersTurn(playerId))
            return BoardResult.WrongTurn;

        short moveAmount = (short)(dto.getLength()-1);
        if (moveAmount<1){
            return BoardResult.NoMove;
        }
        ChangeSpace[] changes;

        if (Move.getColumnDistance((short)0, dto)==1){
            changes=new ChangeSpace[moveAmount*2];
            BoardResult result=tryBasicHop(dto, changes);
            if (result!=BoardResult.Valid){
                return result;
            }
        }
        else{
            changes=new ChangeSpace[moveAmount*3];
            BoardResult result=tryChainHop(dto, moveAmount, changes);
            if (result!=BoardResult.Valid){
                model.getBoard().removeChanges(changes);
                return result;
            }
        }
        try{
            MoveModel newMove = new MoveModel(model, getChangesForDatabase(changes));
            repo.changeFields(model.getId(), !model.isPlayerOneTurn(), newMove);
            turn++;
            model.setPlayerOneTurn(!model.isPlayerOneTurn());
        }catch(Exception e){
            model.getBoard().removeChanges(changes);
            throw e;
        }
        return BoardResult.Valid;
    }

    /**
     * Creates a new array that does not include multiple updates on one field.
     * @param changes The current move changes.
     * @return Changes to put into the database.
     */
    public final ChangeSpace[] getChangesForDatabase(ChangeSpace[] changes){
    //Selecting the same field multiple times in an updates causes an error.

        if (changes.length<=3){
            return changes;
        }

        //Everyone has a spot it from and one it hopped over.
        //Every spot it moved to except the last one is overridden.
        short resultLength=(short)((changes.length/3*2)+1);
        ChangeSpace[] result = new ChangeSpace[resultLength];

        //This indexes the result.
        short j=0;
        for(short i=0; i<changes.length; i++){
            //This skips over the spots the piece moved to.
            if (i%3==1){
                continue;
            }else{
                result[j]=changes[i];
                j++;
            }
        }

        //This sets the place it moved to at the end.
        result[j]=changes[changes.length-2];

        return result;
    }

    public final long getBoardId(){
        return model.getId();
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

    public BoardResult tryBasicHop(MoveDTO dto, ChangeSpace[] changes){
        Move move = new Move((short)0, dto);

        BoardResult test=checkRules(move);
        if (test!=BoardResult.Valid){
            return test;
        }

        if(checkBasicHop(move)){
            ChangeSpace[] hold=handleHop(move);
            changes[0]=hold[0];
            changes[1]=hold[1];
            return BoardResult.Valid;
        }
        else{
            return BoardResult.InvalidHop;
        }
    }

    public BoardResult tryChainHop(MoveDTO dto, short moveAmount, ChangeSpace[] changes){
        Move move;
        BoardResult test;

        for(short i=0; i<moveAmount; i++){
            move = new Move(i, dto);

            test=checkRules(move);
            if (test!=BoardResult.Valid){
                return test;
            }

            //Checks if it a valid move to hop over a piece.
            if (checkHopOver(move)){
                ChangeSpace[] changesSet = handleHopOver(move);
                for (short spot=0; spot<3; spot++){
                    changes[(i*3)+spot]=changesSet[spot];
                }
            }
            else{
                return BoardResult.InvalidHop;
            }
        }
        return BoardResult.Valid;
    }

    /**This handles updates for moving a single space. */
    public ChangeSpace[] handleHop(Move move){
        ChangeSpace[] changes = new ChangeSpace[2];
        updateChange(changes, move);
        model.getBoard().addChanges(changes);
        return changes;
    }

    /**This handles updates for hoping over a piece. */
    public ChangeSpace[] handleHopOver(Move move){
        ChangeSpace[] changes = new ChangeSpace[3];
        updateChange(changes, move);
        updatePad(changes, move);
        model.getBoard().addChanges(changes);
        return changes;
    }

    /**This sets the changes for the space moved from and the space moved over. */
    public void updateChange(ChangeSpace[] changes, Move move){
        //Empty the space the piece started on.
        changes[0] = new ChangeSpace(get(move.currentColumn, move.currentRow), Space.Empty, move.currentColumn, move.currentRow);
        //Filling the space the piece moved to.
        changes[1] = new ChangeSpace(Space.Empty, getWithPromote(move.currentColumn, move.currentRow, move.newColumn), move.newColumn, move.newRow);
    }

    /**This sets the changes for a piece hopped over. */
    public void updatePad(ChangeSpace[] changes, Move move){
        short midColumn;
        short midRow;
        midColumn=move.currentColumn;
        if (move.isDown)
            midColumn++;
        else
            midColumn--;

        midRow = move.getRowIndex((short)1);
        //Emptying the space hopped over.
        changes[2] = new ChangeSpace(get(midColumn, midRow), Space.Empty, midColumn, midRow);
    }

    /**
     * This checks the basic rules except ones for it being a valid movement.
     * @param move The move being checked.
     */
    public BoardResult checkRules(Move move){
        Space piece=get(move.currentColumn, move.currentRow);
        if (!isOwned(piece))
            return BoardResult.NotYours;

        if (!onBoard(move.newColumn, move.newRow))
            return BoardResult.OffBoard;

        if (!checkDirection(piece, !move.isDown))
            return BoardResult.InvalidDirection;

        if (!validTarget(get(move.newColumn, move.newRow))){
            return BoardResult.InvalidTarget;
        }

        return BoardResult.Valid;
    }

    /**The basic method for getting a piece from the board. */
    public final Space get(short column, short row){
        return model.getBoard().getSpace(row, column);
    }
    
    /**
     * Checks if the piece is owned by current player.
     * @param column The column
     * @param row The row
     */
    public boolean isOwned(Space space){
        //No one owns empty spaces.
        if (space==Space.Empty)
            return false;

        if (model.isPlayerOneTurn()){//Player one has red pieces.
            return space.isItRed();
        }
        else{//Player two has black pieces.
            return space.isItBlack();
        }
    }

    /**
     * Checks if the direction being moved is valid for the piece.
     * @param piece The piece being moved.
     * @param isUp Whether the piece is moving up or down in the board.
     */
    public boolean checkDirection(Space piece, boolean isUp){
        if (piece.isKing)
            return true;

        //Red moves up.
        if (isUp)
            return piece.isItRed();
        //Black moves the other way.
        else
            return piece.isItBlack();
    }

    /**
     * Checks that it is on the board.
     * @param column the column of the piece.
     * @param row the row of the piece.
     */
    public boolean onBoard(short column, short row){
        return onColumn(column) & onRow(row);
    }

    /**
     * Checks that it is a valid column.
     * @param column 0 to 7
     */
    public boolean onColumn(short column){
        return column>=0 && column<=7;
    }

    /**
     * Checks that it is a valid row.
     * @param row 0 to 3
     */
    public boolean onRow(short row){
        return row>=0 && row<=3;
    }

    /**
     * Checks if a move was valid based on distance and if hopping over pieces.
     */
    public boolean checkBasicHop(Move move){

        //Can only move a distance of 1.
        if (move.columnDistance!=1 || move.rowDistance!=1)
            return false;

        return true;
    }

    //The differences are recorded so I don't have to recalculate them.
    //This is a split up of checkHop.
    /**
     * Checks if hopping over pieces was valid.
     * @param columnDistance difference between columns
     * @param rowDistance different between rows.
     * @param currentColumn current Column
     * @param currentRow current Row
     * @param newColumn new Column
     * @param newRow new Row
     */
    public boolean checkHopOver(Move move){

        //Hopping over one piece could only move two columns.
        //Them swapping which changes means it balances out to a change of 1.
        if (move.columnDistance!=2 || move.rowDistance!=2)
            return false;

        //The column being hopped over.
        short column=move.getColumnIndex(1);
        short row=move.getRowIndex(1);

        //The space is it going over.
        Space pad=get(column, row);

        //Finally confirms not trying to hop over an owned or empty piece.
        return !(isOwned(pad) || pad==Space.Empty);
    }

    public boolean validTarget(Space piece){
        return piece==Space.Empty;
    }

    /**
     * This is similar to get but it returns a king if piece met requirements to promote.
     * Note: this is meant to be ran AFTER the move.
     * @param move The move being made.  reads nC and nR.
     * @return The piece that should be in the space.
     */
    public Space getWithPromote(short column, short row, short newColumn){
        Space piece = get(column, row);

        if (piece.isRed){
            if (newColumn==0)
                return Space.RedKing;
        }
        else{
            if (newColumn==7)
                return Space.BlackKing;
        }

        return piece;
    }

    public synchronized void raiseViewerCount(){
        viewerCount++;
    }

    public synchronized short lowerViewerCount(){
        viewerCount--;
        return viewerCount;
    }

    @Override
    public final BoardDTO getBoardDTO(long id) {
        return new BoardDTO(model.isPlayerOneTurn(), model.getPlayer1Id()==id, name1, name2, model.getBoard(), model.getStatus(), turn);
    }

    @Override
    public final Board getUpdate(short last) {
        if (last<turn)
            return model.getBoard();
        else
            return null;
    }
}

package checkers.capstone.game.rules.RuleInterfaces;

import checkers.capstone.controllers.tools.exception_stuff.BoardResult;
import checkers.capstone.dtos.BoardDTO;
import checkers.capstone.dtos.MoveDTO;
import checkers.capstone.game.Board;

/**An object for interacting with an individual checkers game. */
public interface TheGame {

    /**This gets the id of a board. */
    long getBoardId();

    /**
     * Checks if player is part of this game.
     * @param playerId The id of player being checked.
     * @return If the player is or not.
     */
    boolean isPlayerParticipant(long playerId);
    
    /**
     * Tries to update the database with a move.
     * @param playerId Id of player making the move.
     * @param dto The data sent in for the move.
     * @return The result of the attempt.
     */
    BoardResult attemptMove(long playerId, MoveDTO dto);

    /**Raises the people accessing this by 1. */
    void raiseViewerCount();

    /**
     * Lowers the people accessing it by 1.
     * @return The new number of people accessing it.
     */
    short lowerViewerCount();

    /**Gets a dto of the game to send to the user. */
    BoardDTO getBoardDTO(long id);

    /**
     * Gets the latest version of the board unless already up to date.
     * @param last The last turn the user viewed.
     * @return Null if up to date the latest board if not.
     */
    Board getUpdate(short last);
}

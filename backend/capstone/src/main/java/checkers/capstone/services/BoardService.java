package checkers.capstone.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import checkers.capstone.dtos.BoardRoomDTO;
import checkers.capstone.game.GameStatus;
import checkers.capstone.game.rules.BaseRules;
import checkers.capstone.game.rules.RuleInterfaces.TheGame;
import checkers.capstone.models.BoardModel;
import checkers.capstone.models.UserModel;
import checkers.capstone.repos.BoardRepo;
import checkers.capstone.repos.UserRepo;
import checkers.capstone.repos.custom.UpdateBoard;

@Service
public class BoardService {
    
    private final BoardRepo boardRepo;
    private final UpdateBoard updateRepo;
    private final Map<Long, TheGame> gameMap = new HashMap<>();
    private final UserRepo userRepo;

    public BoardService(BoardRepo boardRepo, UpdateBoard updateRepo, UserRepo userRepo) {
        this.boardRepo = boardRepo;
        this.updateRepo = updateRepo;
        this.userRepo = userRepo;
    }

    public BoardModel getById(long id){
        return boardRepo.findById(id).get();
    }

    /**
     * Creates a board to the database.
     * @param player1 The player 1.
     * @param player2 The player 2.
     * @return The id of the board.
     */
    public long createBoard(long player1, long player2){
        BoardModel model = new BoardModel(userRepo.findById(player2).get(), userRepo.findById(player1).get());
        return boardRepo.save(model).getId();
    }

    /**
     * Creates a board to the database.  uses name to challenge.
     * @param player1 The name for player 1.
     * @param player2 The name for player 2.
     * @return The id of the board.
     */
    public long createBoard(long player1, String player2){
        BoardModel model = new BoardModel(userRepo.findByUsername(player2).get(), userRepo.findById(player1).get());
        return boardRepo.save(model).getId();
    }

    /**
     * Gets a match the player is in.
     * @param id The id of the match.
     * @return An object for manipulating the game.
     */
    public synchronized TheGame getGameById(long id){
        if (gameMap.containsKey(id)){
            TheGame game = gameMap.get(id);
            game.raiseViewerCount();
            return gameMap.get(id);
        }

        Optional<BoardModel> oModel = boardRepo.findById(id);
        if (!oModel.isPresent())
            return null;

        BoardModel model = oModel.get();
        String name1 = userRepo.findById(model.getPlayer1Id()).get().getUsername();
        String name2 = userRepo.findById(model.getPlayer2Id()).get().getUsername();
        TheGame game = new BaseRules(model, updateRepo, name1, name2);
        gameMap.put(id, game);
        return game;
    }

    /**
     * Stops viewing the room but the game still exists.
     * @param id The id of the game.
     */
    public synchronized void quitViewing(long id){
        if (gameMap.containsKey(id)){
            TheGame rules = gameMap.get(id);
            short count = rules.lowerViewerCount();
            if (count==0){
                gameMap.remove(id);
            }
        }
    }

    /**
     * Gets games a player is participating in.
     * @param playerId The player making the request.
     * @return Dtos containing data for each room.
     */
    public BoardRoomDTO[] getListOfRooms(long playerId){
        BoardModel[] boards = boardRepo.findByPlayer1IdOrPlayer2Id(playerId, playerId);
        BoardRoomDTO[] dtos = new BoardRoomDTO[boards.length];
        GameStatus status;
        long otherPlayerId;
        Optional<UserModel> otherPlayer;
        for(int i=0; i<boards.length; i++){
            otherPlayerId = boards[i].getPlayer1Id();
            boolean youPlayerOne=otherPlayerId == playerId;
            if (otherPlayerId == playerId)
                otherPlayerId = boards[i].getPlayer2Id();

            otherPlayer = userRepo.findById(otherPlayerId);
            status = boards[i].getStatus();
            dtos[i] = new BoardRoomDTO(boards[i].getId(), otherPlayer.get().getUsername(), status, youPlayerOne);
        }

        return dtos;
    }
}

package checkers.capstone.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import checkers.capstone.controllers.tools.exception_stuff.ExceptionProtocols;
import checkers.capstone.controllers.tools.lambdas.BoardCommandLambda;
import checkers.capstone.controllers.tools.session_tools.SessionData;
import checkers.capstone.controllers.tools.session_tools.SessionManager;
import checkers.capstone.dtos.MoveDTO;
import checkers.capstone.game.Board;
import checkers.capstone.game.rules.RuleInterfaces.TheGame;
import checkers.capstone.services.BoardService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService service;

    public BoardController(BoardService service) {
        this.service = service;
    }

    /**
     * This method for creating a new game in the database.
     * @param id The id of the player being challenged.
     * @param request The user's cookies.
     * @return Status created and the id of the new board.
     */
    @PostMapping("/challenge/{id}")
    public ResponseEntity<?> createRoom(@PathVariable long id, HttpServletRequest request){
        SessionData data = SessionManager.getVerifiedSessionData(request);
        if (data==null)
            return ExceptionProtocols.guestUser();
        
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.createBoard(data.getUserId(), id));
        }catch (DataAccessException e){
            System.out.println(e);
            return ExceptionProtocols.databaseMalfunctionedDuringUserAccess();
        }
    }

    /**
     * This is for creating a new game from username.
     * @param name The user being challenged.
     * @param request The user's cookies.
     * @return Status created and the id of the new board.
     */
    @PostMapping("/challenge-name/{name}")
    public ResponseEntity<?> createRoom(@PathVariable String name, HttpServletRequest request){
        SessionData data = SessionManager.getVerifiedSessionData(request);
        if (data==null)
            return ExceptionProtocols.guestUser();
        
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.createBoard(data.getUserId(), name));
        }catch (DataAccessException e){
            System.out.println(e);
            return ExceptionProtocols.databaseMalfunctionedDuringUserAccess();
        }
    }

    /**
     * Gets a list of games you are a part of.
     * @param request The user's cookies.
     * @return Names and such for each game person is a part of.
     */
    @GetMapping("/rooms")
    public ResponseEntity<?> getRoomList(HttpServletRequest request){
        SessionData data = SessionManager.getVerifiedSessionData(request);
        if (data==null)
            return ExceptionProtocols.guestUser();

        try {
            return ResponseEntity.ok(service.getListOfRooms(data.getUserId()));
        }catch (DataAccessException e){
            System.out.println(e);
            return ExceptionProtocols.databaseMalfunctionedDuringUserAccess();
        }
    }

    /**
     * To be marked as joining a room.
     * @param id The id of the game.
     * @param request The user's cookies.
     * @return A dto containing room stuff.
     */
    @GetMapping("/join/{id}")
    public ResponseEntity<?> joinRoom(@PathVariable long id, HttpServletRequest request){
        SessionData data = SessionManager.getVerifiedSessionData(request);
        if (data==null)
            return ExceptionProtocols.guestUser();

        if (data.getGame()!=null){
            data.getGame().lowerViewerCount();
            data.setGame(null);//Just in case something goes wrong with next step.
        }

        try{
            TheGame game = service.getGameById(id);
            data.setGame(game);
            return ResponseEntity.ok("Room joined.");

        }catch (DataAccessException e){
            System.out.println(e);
            return ExceptionProtocols.databaseMalfunctionedDuringUserAccess();
        }
    }

    /**
     * This gets the information to build the room the user is connected to.
     * @param request The user's cookies.
     * @return Information to build the room.
     */
    @GetMapping("/room")
    public ResponseEntity<?> getRoom(HttpServletRequest request){
        SessionData data = SessionManager.getVerifiedSessionData(request);
        if (data==null)
            return ExceptionProtocols.guestUser();

        if (data.getGame()==null)
            return ExceptionProtocols.isNotInARoom();

        try{
            TheGame game = data.getGame();
            return ResponseEntity.ok(game.getBoardDTO(data.getUserId()));
        }catch (DataAccessException e){
            System.out.println(e);
            return ExceptionProtocols.databaseMalfunctionedDuringUserAccess();
        }
    }

    /**
     * This disconnects the user from the room.
     * @param response
     * @param request The user's cookies.
     * @return Just whether it succeeded.
     */
    @DeleteMapping("/leave")
    public ResponseEntity<?> leaveRoom(HttpServletRequest request){
        SessionData data = SessionManager.getVerifiedSessionData(request);
        if (data==null)
            return ExceptionProtocols.guestUser();

        if (data.getGame()!=null){
            data.getGame().lowerViewerCount();
            data.setGame(null);//Just in case something goes wrong with next step.
        }

        return ResponseEntity.ok("You not in a room now.");
    }

    /**
     * This is to make a move in the game the user is playing.
     * @param dto The information regarding the move.
     * @param request The user's cookies.
     * @return Whether the move succeeded.
     */
    @PostMapping(path = "/move")
    public ResponseEntity<?> makeMove(@RequestBody MoveDTO dto, HttpServletRequest request){
        BoardCommandLambda command = (id, game) -> {
            return game.attemptMove(id, dto).message;
        };
        return executeBoardCommand(command, request);
    }

    /**
     * This grabs an update for the user's board.
     * @param latest The latest turn the user is at since updating.
     * @param request The user's cookies.
     * @return An update for the board or no content if user is uptodate.
     */
    @GetMapping("/update/{latest}")
    public ResponseEntity<?> getUpdates(short latest, HttpServletRequest request){
        SessionData data = SessionManager.getVerifiedSessionData(request);
        if (data==null)
            return ExceptionProtocols.guestUser();

        if (data.getGame()==null)
            return ExceptionProtocols.isNotInARoom();

        Board board = data.getGame().getUpdate(latest);
        if (board!=null)
            return ResponseEntity.ok(board);
        else
            return ResponseEntity.noContent().build();
    }

    

    /**
     * The stuff needed to execute updates to the board.
     * @param command The command to execute.
     * @param request The user's cookies.
     * @return Whether the move succeeded.
     */
    private ResponseEntity<?> executeBoardCommand(BoardCommandLambda command, HttpServletRequest request){
        SessionData data = SessionManager.getVerifiedSessionData(request);
        if (data==null)
            return ExceptionProtocols.guestUser();

        if (data.getGame()==null)
            return ExceptionProtocols.isNotInARoom();

        try{
            return command.execute(data.getUserId(), data.getGame());
        }catch (DataAccessException e){
            System.out.println(e);
            return ExceptionProtocols.databaseMalfunctionedDuringUserAccess();
        }
    }
}
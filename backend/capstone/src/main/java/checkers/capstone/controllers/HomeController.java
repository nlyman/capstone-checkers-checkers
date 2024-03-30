package checkers.capstone.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import checkers.capstone.controllers.tools.exception_stuff.ExceptionProtocols;
import checkers.capstone.controllers.tools.exception_stuff.InvalidToken;
import checkers.capstone.controllers.tools.session_tools.SessionData;
import checkers.capstone.controllers.tools.session_tools.SessionManager;
import checkers.capstone.dtos.UserDTO;
import checkers.capstone.services.BoardService;
import checkers.capstone.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class HomeController {
    
    private final UserService service;
    private final BoardService boardService;

    public HomeController(UserService service, BoardService boardService) {
        this.service = service;
        this.boardService = boardService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> postLogin(@RequestBody UserDTO dto, HttpServletResponse response){
        long userId;
        try {
            userId=service.login(dto);
        }catch (DataAccessException e){
            return ExceptionProtocols.databaseMalfunctionedDuringUserAccess();
        }
        if (userId!=0){
            SessionManager.login(response, userId);
            return ResponseEntity.ok("Login successful.");
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }

    /**
     * For logging out the user.
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
        try {
            SessionData data = SessionManager.getVerifiedSessionData(request);
            if (data==null)
                return ResponseEntity.ok("No need.");

            SessionManager.logout(data.getUserId(), response);
            boardService.quitViewing(data.getGame().getBoardId());
            return ResponseEntity.ok("Logout successful.");
        }catch(InvalidToken e){
            SessionManager.logout(SessionManager.getUser(request), response);
            return ResponseEntity.ok("Cleared cookies.");
        }
    }

    /**
     * Checks if user has valid login.
     * @param request The user's cookies.
     * @param response
     * @return Whether the check was authorized.
     */
    @GetMapping("/check")
    public ResponseEntity<?> check(HttpServletRequest request){
        try{
            SessionData data = SessionManager.getVerifiedSessionData(request);
            if (data==null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in.");
            return ResponseEntity.ok("Valid");
        }catch (InvalidToken e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid data.");
        }
    }

    @DeleteMapping("/clear")
    public void clear(HttpServletRequest request, HttpServletResponse response){
        SessionManager.logout(SessionManager.getUser(request), response);
    }
}

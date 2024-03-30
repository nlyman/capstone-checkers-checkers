package checkers.capstone.controllers.tools.exception_stuff;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public enum BoardResult {
    Valid(""),
    WrongTurn("Not your turn."),
    NoMove("No changes found."),
    NotYours("Selected unowned piece."),
    OffBoard("Off board."),
    InvalidDirection("Invalid direction."),
    InvalidTarget("Not valid target to move to."),
    InvalidHop("Invalid hop.");

    public final ResponseEntity<?> message;

    private BoardResult(String message){
        if (message==""){
            this.message=ResponseEntity.status(HttpStatus.CREATED).body("");
        }
        else{
            this.message=ResponseEntity.status(409).body(message);
        }
    }
}

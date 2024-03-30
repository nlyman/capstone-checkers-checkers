package checkers.capstone.controllers.tools.exception_stuff;

import org.springframework.http.ResponseEntity;

public class ExceptionProtocols {
    
    public static ResponseEntity<?> databaseMalfunctionedDuringUserAccess(){
        return ResponseEntity.status(500).body("Something went wrong with the database.");
    }

    public static ResponseEntity<?> guestUser(){
        return ResponseEntity.status(401).body("Not logged in.");
    }

    public static ResponseEntity<?> isNotInARoom(){
        return ResponseEntity.status(401).body("Not in a room.");
    }
}

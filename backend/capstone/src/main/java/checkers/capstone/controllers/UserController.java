package checkers.capstone.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import checkers.capstone.controllers.tools.exception_stuff.ExceptionProtocols;
import checkers.capstone.dtos.UserDTO;
import checkers.capstone.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody UserDTO dto){
        try{
            if (service.createUser(dto))
                return ResponseEntity.status(HttpStatus.CREATED).body("User created.");
            else
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }catch(DataAccessException e){
            return ExceptionProtocols.databaseMalfunctionedDuringUserAccess();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(){
        try{
            return ResponseEntity.ok(service.getAllUsernames());
        }catch(DataAccessException e){
            return ExceptionProtocols.databaseMalfunctionedDuringUserAccess();
        }
    }
}

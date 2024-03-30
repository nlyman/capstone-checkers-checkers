package checkers.capstone.controllers.tools.exception_stuff;

public class InvalidToken extends RuntimeException{
    
    public InvalidToken(String input){
        super(input);
    }
}

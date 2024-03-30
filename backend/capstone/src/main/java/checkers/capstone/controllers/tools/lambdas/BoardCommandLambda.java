package checkers.capstone.controllers.tools.lambdas;

import org.springframework.http.ResponseEntity;

import checkers.capstone.game.rules.RuleInterfaces.TheGame;

@FunctionalInterface
public interface BoardCommandLambda {
    ResponseEntity<?> execute(long userId, TheGame rules);
}

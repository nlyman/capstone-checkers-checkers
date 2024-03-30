package checkers.capstone.repos;

import org.springframework.stereotype.Repository;

import checkers.capstone.game.GameStatus;
import checkers.capstone.models.BoardModel;

@Repository
public interface BoardRepo extends BaseRepo<BoardModel> {
    BoardModel[] findByPlayer1IdOrPlayer2Id(long player1Id, long player2Id);
    long findPlayer1IdById(long id);
    long findPlayer2IdById(long id);
    GameStatus findStatusById(long id);

    interface IdProjection {
        Long getId();
    }
}

package checkers.capstone.repos.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import checkers.capstone.game.rules.ChangeSpace;
import checkers.capstone.models.BoardModel;
import checkers.capstone.models.MoveModel;
import checkers.capstone.repos.MoveRepo;
import jakarta.persistence.*;

@Repository
public class UpdateBoard {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MoveRepo repo;

    /**
     * A method to update board spaces in the table.
     * @param id The model being updated.
     * @param columns The columns.
     * @param rows The rows.
     * @param spaces What the spaces are being changed to.
     */
    public void changeFields(long id, boolean player1, MoveModel model){
        StringBuilder q = new StringBuilder();
        q.append("UPDATE BoardModel b SET playerOneTurn = ?");
        for(ChangeSpace change : model.getChanges()){
            q.append(genUpdate(change.column, change.row));
        }
        q.append(" WHERE b.id = ?");

        TypedQuery<BoardModel> query = entityManager.createQuery(q.toString(), BoardModel.class);
        query.setParameter(1, player1);

        short i=2;
        for (ChangeSpace change : model.getChanges()){
            query.setParameter(i, change.current);
            i++;
        }
        query.setParameter(i, id);
        query.executeUpdate();
        repo.save(model);
    }

    private String genUpdate(short column, short row){
        return ", b.board.row"+row+".column"+column+" = ?";
    }
}

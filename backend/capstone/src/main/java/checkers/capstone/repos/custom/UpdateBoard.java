package checkers.capstone.repos.custom;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import checkers.capstone.game.rules.ChangeSpace;
import checkers.capstone.models.MoveModel;
import jakarta.persistence.*;

@Repository
public class UpdateBoard {

    @PersistenceContext
    private EntityManager entityManager;

    // @Autowired
    // private MoveRepo repo;

    /**
     * A method to update board spaces in the table.
     * @param id The model being updated.
     * @param columns The columns.
     * @param rows The rows.
     * @param spaces What the spaces are being changed to.
     */
    @Transactional
    public void changeFields(long id, boolean player1, MoveModel model){
        StringBuilder q = new StringBuilder();
        q.append("UPDATE BOARD_MODEL b SET PLAYER_ONE_TURN = ?1");
        short i=2;
        for(ChangeSpace change : model.getChanges()){
            q.append(genUpdate(change.column, change.row, i++));
        }
        q.append(" WHERE ID = ?").append(i);

        Query query = entityManager.createNativeQuery(q.toString());
        query.setParameter(1, player1);

        i=2;
        for (ChangeSpace change : model.getChanges()){
            query.setParameter(i++, change.current);
        }
        query.setParameter(i, id);
        query.executeUpdate();
        // repo.save(model);
    }

    /**
     * Generates a string for updating a field.
     * @param column The column of update.
     * @param row The row of update.
     * @param number The position in placement it is at.
     * @return The generated string.
     */
    private String genUpdate(short column, short row, short number){
        return String.format(", \"%dspot%d\" = ?%d", column, row, number);
    }
}

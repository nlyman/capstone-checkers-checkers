package checkers.capstone.rules;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import checkers.capstone.game.Space;
import checkers.capstone.game.rules.BaseRules;
import checkers.capstone.models.BoardModel;
import checkers.capstone.models.MoveModel;
import checkers.capstone.models.UserModel;
import checkers.capstone.repos.custom.UpdateBoard;

public class BaseRulesTest {
    BaseRules rules;
    BoardModel model;

    @Before
    public void setUp(){
        UpdateBoard repo = mock(UpdateBoard.class);
        doAnswer(new Answer<Void> () {
            @Override
            public Void answer(InvocationOnMock invocation){
                
                return null;
            }
        }).when(repo).changeFields(anyLong(), anyBoolean(), any(MoveModel.class));

        UserModel user1 = new UserModel();
        UserModel user2 = new UserModel();
        user1.setId(1);
        user2.setId(2);
        model = new BoardModel(user1, user2);
        rules = new BaseRules(model, repo, null, null);
    }

    @Test
    public void isOwned(){
        model.setPlayerOneTurn(true);
        assertEquals(true, rules.isOwned(Space.Red));
        assertEquals(true, rules.isOwned(Space.RedKing));
        assertEquals(false, rules.isOwned(Space.Black));
        assertEquals(false, rules.isOwned(Space.BlackKing));
        assertEquals(false, rules.isOwned(Space.Empty));

        
        model.setPlayerOneTurn(false);
        assertEquals(false, rules.isOwned(Space.Red));
        assertEquals(false, rules.isOwned(Space.RedKing));
        assertEquals(true, rules.isOwned(Space.Black));
        assertEquals(true, rules.isOwned(Space.BlackKing));
        assertEquals(false, rules.isOwned(Space.Empty));
    }

    @Test
    public void checkDirection(){
        //Check kings.  Should always return true.
        assertEquals(true, rules.checkDirection(Space.RedKing, true));
        assertEquals(true, rules.checkDirection(Space.RedKing, false));
        assertEquals(true, rules.checkDirection(Space.BlackKing, true));
        assertEquals(true, rules.checkDirection(Space.BlackKing, false));

        //Check red piece.  Should return true on up.
        assertEquals(true, rules.checkDirection(Space.Red, true));
        assertEquals(false, rules.checkDirection(Space.Red, false));

        //Check black piece.  Should return true on down.
        assertEquals(true, rules.checkDirection(Space.Black, !true));
        assertEquals(false, rules.checkDirection(Space.Black, !false));
    }

    @Test
    public void onBoard(){
        short columnAmount=8;
        short rowAmount=4;

        for(short column=-20; column<20; column++){
            for(short row=-20; row<20; row++){
                boolean correctResult;
                if (column<0 || column>=columnAmount)
                    correctResult=false;
                else if (row<0 || row>=rowAmount)
                    correctResult=false;
                else
                    correctResult=true;
                assertEquals(correctResult, rules.onBoard(column, row));
            }
        }
    }
}

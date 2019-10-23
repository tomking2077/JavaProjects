package kalah.ai;

import kalah.gamemanager.Board;
import kalah.gamemanager.Move;
import kalah.gamemanager.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class AiManagerTest {

    private AiManager aiManager;

    @Test
    public void testIterativeDeepeningReturnsSameThingAsNormalSearch() {
        /*

        Board:

            1   0   1
        0               0
            2   0   0

         */

        aiManager = new AiManager(Player.PlayerTwo);
        Board board = new Board(0, new int[]{2, 0, 0}, 0, new int[]{1, 0, 1});

        aiManager.setBoard(board, Player.PlayerOne);
        List<Move> bestMoves = aiManager.go(Difficulty.Insane, null);

        assertEquals(1, bestMoves.size());
        assertEquals(Integer.valueOf(0), bestMoves.get(0).getWellNumber().orElse(null));
    }
}

package kalah.ai;

import kalah.gamemanager.Board;
import kalah.gamemanager.Options;
import kalah.gamemanager.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class MinimaxTreeTest {

    private static final Integer TEST_DEPTH = 6;

    private MinimaxTree minimaxTree;

    @Before
    public void setup() {
        final Board board = new Board(new Options(6, 24, false, false));
        minimaxTree = new MinimaxTree(
            board,
            Player.PlayerOne);
    }

    @Test
    public void testMaxReturnsTheSameThingWithAlphaBetaPruning() {
        long startTime = System.nanoTime();
        Integer maxNoPruning = minimaxTree.getMax(Player.PlayerOne);
        long endTime = System.nanoTime();
        long noPruningTime = endTime - startTime;

        startTime = System.nanoTime();
        Integer maxWithPruning = minimaxTree.getMaxWithPruning(Player.PlayerOne);
        endTime = System.nanoTime();

        long pruningTime = endTime - startTime;

        assertEquals(maxNoPruning, maxWithPruning);

        System.out.println("testMax difference");
        System.out.println("No Pruning: " + noPruningTime);
        System.out.println("Pruning   : " + pruningTime);
    }

    @Test
    public void testMinReturnsTheSamThingWithAlphaBetaPruning() {
        long startTime = System.nanoTime();
        Integer minNoPruning = minimaxTree.getMin(Player.PlayerOne);
        long endTime = System.nanoTime();
        long noPruningTime = endTime - startTime;

        startTime = System.nanoTime();
        Integer minWithPruning = minimaxTree.getMinWithPruning(Player.PlayerOne);
        endTime = System.nanoTime();

        long pruningTime = endTime - startTime;

        assertEquals(minNoPruning, minWithPruning);

        System.out.println("testMin difference");
        System.out.println("No Pruning: " + noPruningTime);
        System.out.println("Pruning   : " + pruningTime);
    }
}

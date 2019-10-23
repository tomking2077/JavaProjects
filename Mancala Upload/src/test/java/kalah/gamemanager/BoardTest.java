package kalah.gamemanager;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BoardTest {
    Board board;

    @Test
    public void EndMarblesTest() {
        board = new Board(new Options(6, 4, false, false));
        MoveType move = board.moveMarbles(Player.PlayerOne, 2);
        assertEquals(MoveType.HouseMove, move);
        assertEquals(0, board.getWellMarbles(Player.PlayerOne, 2));
        assertEquals(5, board.getWellMarbles(Player.PlayerOne, 3));
        assertEquals(5, board.getWellMarbles(Player.PlayerOne, 4));
        assertEquals(5, board.getWellMarbles(Player.PlayerOne, 5));
    }

    @Test
    public void CaptureMarblesTest() {
        board = new Board(new Options(6, 4, false, false));
        board.moveMarbles(Player.PlayerOne, 5);
        board.moveMarbles(Player.PlayerOne, 1);
        assertEquals(0, board.getWellMarbles(Player.PlayerOne, 5));
        assertEquals(0, board.getWellMarbles(Player.PlayerTwo, 0));
        assertEquals(7, board.getHouseMarbles(Player.PlayerOne));
    }

    @Test
    public void DeepCloneTest() {
        final int TEST_WELL = 0;

        board = new Board(new Options(6, 4, false, false));
        Board newBoard = board.clone();
        board.moveMarbles(Player.PlayerOne, TEST_WELL);

        assertNotEquals(board.getWellMarbles(Player.PlayerOne, TEST_WELL),
            newBoard.getWellMarbles(Player.PlayerOne, TEST_WELL));
    }
}
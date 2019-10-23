package kalah.ai;

import kalah.gamemanager.Board;
import kalah.gamemanager.Move;
import kalah.gamemanager.Player;
import kalah.gamemanager.UserMoveType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class MinimaxNodeTest {
    private final static Player TEST_PLAYER = Player.PlayerTwo;
    private final static List<Move> TEST_MOVES_LIST = Collections.singletonList(new Move(UserMoveType.WellMove, 0));

    Board board;

    MinimaxNode minimaxNode;

    @Test
    public void test() {
    }

    public void generateChildrenWithOnePossibleNonHouseMoveTest() {
        /*

            0   0   0   0   0   5
        0                           0
            0   0   0   1   0   0

            Should generate

            5
            |
            3
         */

        board = new Board(0, new int[]{0, 0, 0, 1, 0, 0},
            0, new int[]{5, 0, 0, 0, 0, 0});

        minimaxNode = new MinimaxNode(board, TEST_PLAYER, TEST_MOVES_LIST);

        minimaxNode.generateChildren();

        assertEquals(1, minimaxNode.getChildren().size());
        assertEquals(1, minimaxNode.getChildren().get(0).getMoves().size());
        assertEquals(Integer.valueOf(3), minimaxNode.getChildren().get(0).getMoves().get(0));
    }

    public void generateChildrenWithMultipleHouseMovesTest() {
        /*

            0   0   0   0   0   5
        0                           0
            0   0   4   0   1   0

            Should generate

                                 5
             ____________________|__________________________
             |     |      |      |       |         |       |
            2,3  2,4,3  2,4,5  2,5,3  2,5,4,3  2,5,4,5,3   4

         */

        List<List<Integer>> expectedMoves = new ArrayList<>() {{
            add(new ArrayList<>(Arrays.asList(2, 3)));
            add(new ArrayList<>(Arrays.asList(2, 4, 3)));
            add(new ArrayList<>(Arrays.asList(2, 4, 5)));
            add(new ArrayList<>(Arrays.asList(2, 5, 3)));
            add(new ArrayList<>(Arrays.asList(2, 5, 4, 3)));
            add(new ArrayList<>(Arrays.asList(2, 5, 4, 5, 3)));
            add(Collections.singletonList(4));
        }};

        board = new Board(0, new int[]{0, 0, 4, 0, 1, 0},
            0, new int[]{5, 0, 0, 0, 0, 0});
        minimaxNode = new MinimaxNode(board, TEST_PLAYER, TEST_MOVES_LIST);

        minimaxNode.generateChildren();

        assertEquals(7, minimaxNode.getChildren().size());
        for (int i = 0; i < expectedMoves.size(); ++i) {
            assertEquals(expectedMoves.get(i), minimaxNode.getChildren().get(i).getMoves());
        }
    }
}

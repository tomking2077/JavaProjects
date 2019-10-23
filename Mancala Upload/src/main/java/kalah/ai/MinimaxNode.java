package kalah.ai;

import kalah.gamemanager.Board;
import kalah.gamemanager.Move;
import kalah.gamemanager.MoveType;
import kalah.gamemanager.Player;
import kalah.gamemanager.UserMoveType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class MinimaxNode {
    private final static int MARBLE_IN_HOUSE_MULTIPLIER = 3;
    private final static int MARBLE_DIFFERENCE_MULTIPLIER = 1;
    private final List<MinimaxNode> children;
    private Board board;
    private Player player;
    private List<Move> moves;

    MinimaxNode(Board board, Player player, List<Move> moves) {
        this.board = board;
        this.player = player;
        this.moves = moves;
        this.children = new ArrayList<>();
    }

    int getUtility(Player player) {
        if (board.isGameOver()) {
            if (board.getWinner() == player) {
                return Integer.MAX_VALUE;
            } else if (board.getWinner() == player.flipPlayer()) {
                return Integer.MIN_VALUE;
            } else {
                return 0;
            }
        }

        return
            (board.getHouseMarbles(player) - board.getHouseMarbles(player.flipPlayer())) * MARBLE_IN_HOUSE_MULTIPLIER +
                (board.getWellMarbles(player) - board.getWellMarbles(player.flipPlayer())) * MARBLE_DIFFERENCE_MULTIPLIER;
    }

    List<MinimaxNode> getChildren() {
        return children;
    }

    List<Move> getMoves() {
        return moves;
    }

    void generateChildren() {
        generateChildren(null);
    }

    void generateChildren(Long stopTime) {
        if (children.size() > 0) {
            throw new RuntimeException("kalah.ai.MinimaxNode.generateChildren() failed: children not empty");
        }

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

        for (Move move : board.getValidMoves(player.flipPlayer())) {
            if (stopTime != null && System.currentTimeMillis() >= stopTime) {
                executor.shutdownNow();
                return;
            }

            executor.submit(() -> addChildFromMove(board, player.flipPlayer(), Collections.singletonList(move), stopTime));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ignored) {
        } finally {
            executor.shutdownNow();
        }
    }

    private void addChildFromMove(
        Board currentBoard,
        Player currentPlayer,
        List<Move> movesList,
        Long stopTime) {

        if (stopTime != null && System.currentTimeMillis() >= stopTime) {
            return;
        }

        Board afterMoveBoard = currentBoard.clone();

        Move move = movesList.get(movesList.size() - 1);
        MoveType moveType;

        if (move.getUserMoveType() == UserMoveType.WellMove) {
            if (!move.getWellNumber().isPresent()) {
                throw new RuntimeException("kalah.ai.MinimaxNode.addChildFromMove() failed: no well present");
            }

            moveType = afterMoveBoard.moveMarbles(currentPlayer, move.getWellNumber().get());
        } else if (move.getUserMoveType() == UserMoveType.PieMove) {
            afterMoveBoard.pieMove();
            moveType = MoveType.PieMove;
        } else {
            throw new RuntimeException("kalah.ai.MinimaxNode.addChildFromMove() failed: undefined move type");
        }

        if (moveType == MoveType.HouseMove && !afterMoveBoard.isGameOver()) {
            for (Move wellMove : afterMoveBoard.getValidMoves(currentPlayer)) {
                if (stopTime != null && System.currentTimeMillis() >= stopTime) {
                    return;
                }

                List<Move> newList = new ArrayList<>(movesList);
                newList.add(wellMove);
                addChildFromMove(afterMoveBoard, currentPlayer, newList, stopTime);
            }
        } else {
            synchronized (children) {
                children.add(new MinimaxNode(afterMoveBoard, currentPlayer, movesList));
            }
        }
    }

    Boolean isLeaf() {
        return board.getValidMoves(player.flipPlayer()).isEmpty();
    }
}

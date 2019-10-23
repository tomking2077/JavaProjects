package kalah.ai;

import kalah.gamemanager.Board;
import kalah.gamemanager.Move;
import kalah.gamemanager.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class AiManager {
    private Random rng;
    private MinimaxTree minimaxTree;
    private Player aiPlayer;

    public AiManager(Player aiPlayer) {
        this.aiPlayer = aiPlayer;
        rng = new Random();
    }

    public void setBoard(Board board, Player playerWhoJustMoved) {
        minimaxTree = new MinimaxTree(board, playerWhoJustMoved);
    }

    public List<Move> go(Difficulty difficulty, Long millis) {
        Long stopTime = (millis == null) ? Long.MAX_VALUE : System.currentTimeMillis() + millis;

        // get immediately available moves
        minimaxTree.getHead().generateChildren();

        List<Move> bestMoves = new ArrayList<>();

        // random move
        if (difficulty == Difficulty.Easy) {
            int rand = rng.nextInt(minimaxTree.getHead().getChildren().size());
            for (int i = 0; i < minimaxTree.getHead().getChildren().size(); ++i) {
                if (i == rand) {
                    return minimaxTree.getHead().getChildren().get(i).getMoves();
                }
            }

            return null;
        }

        // get worst moves
        if (difficulty == Difficulty.VeryEasy) {
            Optional<Integer> currentMin = Optional.empty();
            List<Move> currentWorstMoves = null;
            for (MinimaxNode node : minimaxTree.getHead().getChildren()) {
                Integer nodeUtility = node.getUtility(aiPlayer);
                if (!currentMin.isPresent() || currentMin.get() > nodeUtility) {
                    currentMin = Optional.of(nodeUtility);
                    currentWorstMoves = node.getMoves();
                }
            }

            return currentWorstMoves;
        }

        Integer currentDepth = 1;
        Optional<Integer> previousDepthBest = Optional.empty();

        search_depth:
        while (currentDepth < difficulty.getDepth() && System.currentTimeMillis() < stopTime) {
            minimaxTree.setMaxDepth(currentDepth);

            Optional<Integer> currentBest = Optional.empty();
            List<Move> currentDepthBestMoves = new ArrayList<>();
            for (MinimaxNode node : minimaxTree.getHead().getChildren()) {
                if (System.currentTimeMillis() >= stopTime) {
                    System.out.println("time out");
                    break search_depth;
                }

                Integer minValue = minimaxTree.getMinWithPruning(node, 1, aiPlayer, stopTime);

                if (minValue == null) {
                    break search_depth;
                }

                System.out.println("info depth " + currentDepth + " move " + node.getMoves() + " minValue " + minValue);
                if (!currentBest.isPresent() || minValue > currentBest.get()) {
                    currentBest = Optional.of(minValue);
                    currentDepthBestMoves = node.getMoves();
                }

                // best moves is current possible best
                if (previousDepthBest.isPresent() && currentBest.get() > previousDepthBest.get()) {
                    bestMoves = node.getMoves();
                }

                if (minValue == Integer.MAX_VALUE) {
                    return currentDepthBestMoves;
                }
            }

            bestMoves = currentDepthBestMoves;
            System.out.println("Best move after " + currentDepth + ": " + bestMoves + " (" + currentBest.orElse(null) + ")");

            ++currentDepth;
            previousDepthBest = currentBest;
        }

        return bestMoves;
    }
}

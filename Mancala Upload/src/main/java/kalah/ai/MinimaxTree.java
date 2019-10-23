package kalah.ai;

import kalah.gamemanager.Board;
import kalah.gamemanager.Player;

import java.util.Collections;

class MinimaxTree {
    private MinimaxNode head;
    private Integer maxDepth;

    MinimaxTree(
        Board board,
        Player player) {

        head = new MinimaxNode(board, player, Collections.emptyList());
        maxDepth = 0;
    }

    MinimaxNode getHead() {
        return head;
    }

    void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Deprecated
    Integer getMax(Player player) {
        return getMax(head, 0, player);
    }

    @Deprecated
    Integer getMin(Player player) {
        return getMin(head, 0, player);
    }

    @Deprecated
    Integer getMaxWithPruning(Player player) {
        return getMaxWithPruning(head, 0, player);
    }

    Integer getMaxWithPruning(MinimaxNode node, int depthOfNode, Player player) {
        return getMax(node, depthOfNode, Integer.MIN_VALUE, Integer.MAX_VALUE, player, null);
    }

    @Deprecated
    Integer getMinWithPruning(Player player) {
        return getMinWithPruning(head, 0, player, null);
    }

    Integer getMinWithPruning(MinimaxNode node, int depthOfNode, Player player, Long stopTime) {
        return getMin(node, depthOfNode, Integer.MIN_VALUE, Integer.MAX_VALUE, player, stopTime);
    }

    @Deprecated
    private Integer getMax(MinimaxNode node, int depth, Player player) {
        if (isTerminal(node, depth)) {
            return node.getUtility(player);
        }

        Integer result = Integer.MIN_VALUE;
        for (MinimaxNode child : node.getChildren()) {
            result = Integer.max(result, getMin(child, depth + 1, player));
        }

        return result;
    }

    private Integer getMax(MinimaxNode node, int depth, int alpha, int beta, Player player, Long stopTime) {
        if (stopTime != null && System.currentTimeMillis() >= stopTime) {
            return null;
        }

        if (isTerminal(node, depth)) {
            return node.getUtility(player);
        } else if (node.getChildren().isEmpty()) {
            node.generateChildren();
        }

        Integer result = Integer.MIN_VALUE;
        for (MinimaxNode child : node.getChildren()) {
            if (stopTime != null && System.currentTimeMillis() >= stopTime) {
                return null;
            }

            if (child == null) {
                continue;
            }

            Integer min = getMin(child, depth + 1, alpha, beta, player, stopTime);
            if (min == null) {
                return null;
            } else {
                result = Integer.max(result, min);
                alpha = Integer.max(result, alpha);

                if (result > beta) {
                    return result;
                }
            }
        }

        return result;
    }

    @Deprecated
    private Integer getMin(MinimaxNode node, int depth, Player player) {
        if (isTerminal(node, depth)) {
            return node.getUtility(player);
        }

        Integer result = Integer.MAX_VALUE;
        for (MinimaxNode child : node.getChildren()) {
            result = Integer.min(result, getMax(child, depth + 1, player));
        }

        return result;
    }

    private Integer getMin(MinimaxNode node, int depth, int alpha, int beta, Player player, Long stopTime) {
        if (stopTime != null && System.currentTimeMillis() >= stopTime) {
            return null;
        }

        if (isTerminal(node, depth)) {
            return node.getUtility(player);
        } else if (node.getChildren().isEmpty()) {
            node.generateChildren(stopTime);
        }

        Integer result = Integer.MAX_VALUE;
        for (MinimaxNode child : node.getChildren()) {
            if ((stopTime != null && System.currentTimeMillis() >= stopTime)) {
                return null;
            }

            if (child == null) {
                continue;
            }

            Integer max = getMax(child, depth + 1, alpha, beta, player, stopTime);
            if (max == null) {
                return null;
            } else {
                result = Integer.min(result, max);
                beta = Integer.min(result, beta);

                if (result < alpha) {
                    return result;
                }
            }
        }

        return result;
    }

    private Boolean isTerminal(MinimaxNode node, int depth) {
        return node.isLeaf() || depth >= maxDepth;
    }
}

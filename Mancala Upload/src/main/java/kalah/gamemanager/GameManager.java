package kalah.gamemanager;

import kalah.networking.Info;

public class GameManager {
    private Board board;
    private Player activePlayer;
    private GameState gameState;
    private boolean pieRule;

    public GameManager(Options options) {
        this.pieRule = options.isPieRule();
        board = new Board(options);
        activePlayer = Player.PlayerOne;
        gameState = GameState.NewGame;
    }

    public GameManager(Info info) {
        this.pieRule = true;
        board = new Board(0, info.getWellDistribution().clone(), 0, info.getWellDistribution().clone());
        activePlayer = Player.PlayerOne;
        gameState = GameState.NewGame;
    }

    public static void printMessageBasedOnMoveType(MoveType moveType, Player player) {
        switch (moveType) {
            case CaptureMove:
                System.out.println(player + " captured the enemies marbles!");
                break;
            case PieMove:
                System.out.println(player + " played pie move");
                break;
            case StandardMove:
                break;
            case HouseMove:
                System.out.println(player + " earned an extra turn!");
                break;
            case IllegalMove:
                System.out.println(player + " played an illegal move!");
                break;
        }
    }

    public static void printPostGameResults(Player winningPlayer, GameManager gameManager) {
        System.out.println("Player One Score: " + gameManager.getScore(Player.PlayerOne));
        System.out.println("Player Two Score: " + gameManager.getScore(Player.PlayerTwo));
        if (winningPlayer == null) {
            System.out.println("Game ended in a tie!");
        } else {
            System.out.println(winningPlayer + " won!");
        }
    }

    public Board getBoard() {
        return board;
    }

    public void printBoardState() {
        board.printBoardState();
    }

    public GameState getGameState() {
        return gameState;
    }

    private int getScore(Player player) {
        return board.getHouseMarbles(player);
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public Player getWinner() {
        return board.getWinner();
    }

    public MoveType makeMove(Move move) {
        switch (move.getUserMoveType()) {
            case WellMove:
                if (move.getWellNumber().isPresent()) {
                    return marbleMove(move.getWellNumber().get());
                } else {
                    return MoveType.IllegalMove;
                }
            case PieMove:
                if (gameState == GameState.PieEligible) {
                    return pieMove();
                } else {
                    return MoveType.IllegalMove;
                }
            default:
                return MoveType.IllegalMove;
        }
    }

    private MoveType pieMove() {
        if (gameState == GameState.PieEligible) {
            board.pieMove();
            activePlayer = activePlayer.flipPlayer();
            progressGameState(MoveType.PieMove, true);
            return MoveType.PieMove;
        } else {
            return MoveType.IllegalMove;
        }
    }

    private MoveType marbleMove(int wellNumber) {
        MoveType moveType = board.moveMarbles(activePlayer, wellNumber);
        boolean endingTurn = false;
        switch (moveType) {
            case CaptureMove:
            case StandardMove:
                activePlayer = activePlayer.flipPlayer();
                endingTurn = true;
                break;
            case HouseMove:
            case IllegalMove:
                break;
        }
        progressGameState(moveType, endingTurn);

        if (gameState == GameState.GameOver) {
            activePlayer = null;
        }

        return moveType;
    }

    private void progressGameState(MoveType moveType, boolean endingTurn) {
        //Don't progress game state if illegal
        if (moveType == MoveType.IllegalMove) {
            return; //Can't progress gameState
        }
        //Check if Game Over
        if (board.isGameOver()) {
            gameState = GameState.GameOver;
            return;
        }
        //Keep Player Two from making Pie Move after House Move
        if (gameState == GameState.PieEligible && activePlayer == Player.PlayerTwo) {
            gameState = GameState.InProgress;
        }
        //Check if first move
        if (gameState == GameState.NewGame && pieRule && endingTurn) {
            gameState = GameState.PieEligible;
        } else if (endingTurn) {
            gameState = GameState.InProgress;
        }
    }
}

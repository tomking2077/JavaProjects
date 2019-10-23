package kalah.networking;

import kalah.gamemanager.GameState;
import kalah.gamemanager.Move;
import kalah.gamemanager.MoveType;
import kalah.gamemanager.Player;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static kalah.networking.Client.parseMoveString;

public class ConsoleServer extends Server {

    public ConsoleServer(int portNumber, Info info) throws InvalidCommandException {
        super(portNumber, info);
    }

    public static void main(String[] args) throws IOException, InvalidCommandException {
        new ConsoleServer(Integer.parseInt(args[0]), new Info(6, 4, false, 10000));
    }

    @Override
    protected void init() {
        System.out.println("Server open on " + serverSocket.getLocalSocketAddress().toString());
    }

    @Override
    protected void launchGame() throws IOException, InvalidCommandException {
        while (gameManager.getGameState() != GameState.GameOver) {
            Player activePlayer = gameManager.getActivePlayer();
            String moveString = "";
            if (info.getMsTime() > 0) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Future<String> future = executor.submit(new TimedMessageReaderFromActivePlayer());
                try {
                    moveString = future.get(info.getMsTime(), TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    future.cancel(true);
                    timeOutAbortion(activePlayer);
                }
            } else {
                moveString = receiveMessage(activePlayer);
            }
            sendOk(activePlayer);
            boolean illegalMove = false;
            try {
                List<Move> playerMoves = parseMoveString(moveString);
                for (Move playerMove : playerMoves) {
                    MoveType moveType = gameManager.makeMove(playerMove);
                    if (moveType == MoveType.IllegalMove) {
                        illegalMove = true;
                        break;
                    }
                }
            } catch (Exception e) {
                //Handles illegal formatting, but not an illegal move
                illegalAbortion(activePlayer);
            }
            if (illegalMove) {
                illegalAbortion(activePlayer);
            }
            sendMessage(activePlayer.flipPlayer(), moveString);
            readOk(activePlayer.flipPlayer());
        }
        standardAbortion();
    }
}

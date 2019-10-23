package kalah.gui;

import kalah.gamemanager.GameState;
import kalah.gamemanager.Move;
import kalah.gamemanager.MoveType;
import kalah.gamemanager.Player;
import kalah.networking.Info;
import kalah.networking.InvalidCommandException;
import kalah.networking.Server;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static kalah.networking.Client.parseMoveString;

public class GuiServer extends Server {
    private JFrame mainFrame;
    private WindowGame gamePanel;

    GuiServer(int portNumber, Info info) throws InvalidCommandException {
        super(portNumber, info);
    }

    public static void main(String[] args) throws IOException, InvalidCommandException {
        new GuiServer(Integer.parseInt(args[0]), new Info(6, 4, false, 10000));
    }

    @Override
    protected void init() {
        System.out.println("Server open on " + serverSocket.getLocalSocketAddress().toString());

        mainFrame = new JFrame();
        mainFrame.setTitle("Waiting for players...");

        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainFrame.dispose();
                System.exit(0);
            }
        });
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setSize(Window.WIDTH, Window.HEIGHT);
        mainFrame.setVisible(true);

        gamePanel = new WindowGame(gameManager, (long) info.getMsTime());
    }

    @Override
    protected void launchGame() throws IOException, InvalidCommandException {
        mainFrame.setTitle("Spectator");
        mainFrame.setContentPane(gamePanel);
        mainFrame.pack();
        mainFrame.revalidate();

        gamePanel.paintImmediately(0, 0, gamePanel.getWidth(), gamePanel.getHeight());

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

                    gamePanel.paintImmediately(0, 0, gamePanel.getWidth(), gamePanel.getHeight());

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

        showEndScreen();
        standardAbortion();
    }

    private void showEndScreen() {
        JPanel endPanel = new WindowEnd(null, gameManager.getWinner(), false);

        mainFrame.remove(gamePanel);
        mainFrame.setContentPane(endPanel);
        mainFrame.pack();
        mainFrame.revalidate();
    }
}

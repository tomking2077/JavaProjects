package kalah.gui;

import kalah.ai.AiManager;
import kalah.ai.Difficulty;
import kalah.gamemanager.GameManager;
import kalah.gamemanager.GameState;
import kalah.gamemanager.Move;
import kalah.gamemanager.Options;
import kalah.gamemanager.Player;
import kalah.networking.Acknowledgement;
import kalah.networking.Client;
import kalah.networking.ConnectionInformation;
import kalah.networking.Info;
import kalah.networking.InvalidCommandException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

class GuiAiClient {
    private static final Long TIME_BUFFER = 500L;
    private Client client;
    private GameManager gameManager;
    private AiManager aiManager;
    private Player player;
    private JFrame mainFrame;
    private WindowGame gamePanel;
    private Long timeToSearch;

    GuiAiClient(ConnectionInformation connectionInformation) throws IOException, InvalidCommandException {
        client = new Client(connectionInformation);
        client.readWelcome();

        mainFrame = new JFrame();
        mainFrame.setTitle("Waiting for another player...");

        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    client.close();
                    mainFrame.dispose();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setSize(Window.WIDTH, Window.HEIGHT);
        mainFrame.setVisible(true);

        Info info = client.readInfo();
        int msTime = info.getMsTime();
        player = info.getPlayer();
        gameManager = new GameManager(info);
        aiManager = new AiManager(player);

        timeToSearch = (msTime == 0) ? Options.DEFAULT_CLIENT_AI_MOVE_TIME : (msTime - TIME_BUFFER);

        gamePanel = new WindowGame(gameManager, (long) msTime);

        mainFrame.setTitle("AI Client (" + player.toString() + ")");
        mainFrame.setContentPane(gamePanel);
        mainFrame.pack();
        mainFrame.revalidate();

        gamePanel.paintImmediately(0, 0, gamePanel.getWidth(), gamePanel.getHeight());

        client.sendReady();

        launchGame();
    }

    public static void main(String[] args) throws IOException, InvalidCommandException {
        SwingUtilities.invokeLater(() -> {
            try {
                new GuiAiClient(new ConnectionInformation(args[0], Integer.parseInt(args[1])));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void launchGame() throws IOException, InvalidCommandException {
        while (true) {
            if (player == gameManager.getActivePlayer() && gameManager.getGameState() != GameState.GameOver) {
                aiManager.setBoard(gameManager.getBoard(), player.flipPlayer());
                List<Move> moves = aiManager.go(Difficulty.Infinite, timeToSearch);
                client.sendMoves(moves);

                for (Move move : moves) {
                    gameManager.makeMove(move);
                }
                gamePanel.paintImmediately(0, 0, gamePanel.getWidth(), gamePanel.getHeight());

                String response = client.receiveMessage();
                if (Acknowledgement.isGameEndingAcknowledgement(response)) {
                    endGame(response);
                    return;
                }
            } else {
                String response = client.receiveMessage();
                if (Acknowledgement.isGameEndingAcknowledgement(response)) {
                    endGame(response);
                    return;
                } else {
                    List<Move> moves = Client.parseMoveString(response);
                    for (Move move : moves) {
                        gameManager.makeMove(move);
                    }

                    gamePanel.paintImmediately(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
                    client.sendOk();

                    if (gameManager.getGameState() == GameState.GameOver) {
                        response = client.receiveMessage();
                        if (Acknowledgement.isGameEndingAcknowledgement(response)) {
                            endGame(response);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void endGame(String response) throws IOException {
        String result;
        if (response.equals(Acknowledgement.ILLEGAL.name())) {
            System.out.println("ILLEGAL MOVE MADE!");
            result = client.receiveMessage();
        } else if (response.equals(Acknowledgement.TIME.name())) {
            System.out.println("TIME OUT!");
            result = client.receiveMessage();
        } else {
            result = response;
        }

        Player winningPlayer = null;
        switch (Acknowledgement.valueOf(result)) {
            case LOSER:
                winningPlayer = player.flipPlayer();
                System.out.println("YOU LOST");
                break;
            case TIE:
                System.out.println("TIE");
                break;
            case WINNER:
                winningPlayer = player;
                System.out.println("YOU WON");
                break;
        }

        WindowEnd endPanel = new WindowEnd(player, winningPlayer, false);

        mainFrame.remove(gamePanel);
        mainFrame.setContentPane(endPanel);
        mainFrame.pack();
        mainFrame.revalidate();
        endPanel.paintImmediately(0, 0, endPanel.getWidth(), endPanel.getHeight());
    }
}


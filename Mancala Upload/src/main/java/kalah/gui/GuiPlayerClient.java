package kalah.gui;

import kalah.gamemanager.GameManager;
import kalah.gamemanager.GameState;
import kalah.gamemanager.Move;
import kalah.gamemanager.MoveType;
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
import java.util.ArrayList;
import java.util.List;

class GuiPlayerClient {
    private Client client;

    private GameManager gameManager;
    private Player player;
    private int msTime;

    private JFrame mainFrame;
    private WindowGame gamePanel;

    private List<Move> currentMovesMade;

    GuiPlayerClient(ConnectionInformation connectionInformation) throws IOException, InvalidCommandException {
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
        msTime = info.getMsTime();
        player = info.getPlayer();
        gameManager = new GameManager(info);
        currentMovesMade = new ArrayList<>();

        setupGamePanel();

        mainFrame.setTitle("Player Client (" + player.toString() + ")");
        mainFrame.setContentPane(gamePanel);
        mainFrame.pack();

        client.sendReady();

        if (player == Player.PlayerTwo) {
            gamePanel.paintImmediately(0, 0, gamePanel.getWidth(), gamePanel.getHeight());

            String response = client.receiveMessage();
            if (Acknowledgement.isGameEndingAcknowledgement(response)) {
                endGame(response);
            } else {
                List<Move> moveList = Client.parseMoveString(response);
                for (Move m : moveList) {
                    makeMove(m);
                }
                client.sendOk();
            }
        }
    }

    public static void main(String[] args) throws IOException, InvalidCommandException {
        SwingUtilities.invokeLater(() -> {
            try {
                new GuiPlayerClient(new ConnectionInformation(args[0], Integer.parseInt(args[1])));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void setupGamePanel() {
        gamePanel = new WindowGame(gameManager, (long) msTime);
        WindowGameButtonSetupUtils gameButtonSetup = new WindowGameButtonSetupUtils(gamePanel, gameManager);
        gameButtonSetup.setupPieRuleButton(player, this::makeMove);
        gameButtonSetup.setupSubmitButton(player, this::makeMove);
        gameButtonSetup.setupMouseListener(player, this::makeMove);
    }

    private Void makeMove(Move move) {
        Player activePlayer = gameManager.getActivePlayer();
        MoveType moveType = gameManager.makeMove(move);

        System.out.println(activePlayer + " played: " + move);

        GameManager.printMessageBasedOnMoveType(moveType, activePlayer);
        if (moveType == MoveType.IllegalMove) {
            return null;
        } else if (player == activePlayer) {
            currentMovesMade.add(move);
        }

        gamePanel.resetMoveField();
        gamePanel.setMoveFieldErrorMessage(null);
        gamePanel.setButtonEnabled(WindowGame.Button.PieRuleButton, gameManager.getGameState() == GameState.PieEligible);
        gamePanel.paintImmediately(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
        gameManager.printBoardState();

        try {
            if (gameManager.getGameState() == GameState.GameOver) {
                gamePanel.paintImmediately(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
                GameManager.printPostGameResults(gameManager.getWinner(), gameManager);
            }

            // if end of turn
            if (activePlayer != gameManager.getActivePlayer() && activePlayer == player) {
                client.sendMoves(currentMovesMade);
                currentMovesMade.clear();
                String response = client.receiveMessage();
                if (Acknowledgement.isGameEndingAcknowledgement(response)) {
                    endGame(response);
                    return null;
                }

                if (gameManager.getGameState() == GameState.GameOver) {
                    response = client.receiveMessage();
                    if (Acknowledgement.isGameEndingAcknowledgement(response)) {
                        endGame(response);
                        return null;
                    }
                }

                response = client.receiveMessage();
                if (Acknowledgement.isGameEndingAcknowledgement(response)) {
                    endGame(response);
                    return null;
                } else {
                    List<Move> moveList = Client.parseMoveString(response);
                    for (Move m : moveList) {
                        makeMove(m);
                    }
                    client.sendOk();

                    if (gameManager.getGameState() == GameState.GameOver) {
                        response = client.receiveMessage();
                        if (Acknowledgement.isGameEndingAcknowledgement(response)) {
                            endGame(response);
                            return null;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
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
                break;
            case TIE:
                break;
            case WINNER:
                winningPlayer = player;
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

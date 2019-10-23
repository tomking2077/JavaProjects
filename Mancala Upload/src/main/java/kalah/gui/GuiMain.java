package kalah.gui;

import kalah.ai.AiManager;
import kalah.ai.Difficulty;
import kalah.gamemanager.GameManager;
import kalah.gamemanager.GameState;
import kalah.gamemanager.Move;
import kalah.gamemanager.MoveType;
import kalah.gamemanager.Options;
import kalah.gamemanager.Player;
import kalah.networking.ConnectionInformation;
import kalah.networking.ConsoleServer;
import kalah.networking.Info;
import kalah.networking.InvalidCommandException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class GuiMain {
    private JFrame mainFrame;
    private WindowIntroduction introPanel;
    private WindowRules rulesPanel;
    private WindowLocalGameModes localGameModesPanel;
    private WindowGameOptions optionsPanel;
    private WindowConnectionOptions connectionOptionsPanel;
    private WindowConnections hostServerPanel;
    private WindowConnections connectToServerPanel;
    private WindowServerClientModes serverClientModesPanel;
    private WindowGame gamePanel;
    private WindowEnd endPanel;
    private Clip audioClip;
    private boolean volumeMute = false;

    private GameManager gameManager;
    private AiManager aiManager;

    // local mode specific
    private Options localGameOptions;
    private Player aiPlayer;
    private Difficulty aiDifficulty;

    // server/client stuff
    private ConnectionInformation connectionInformation;
    private Info serverGameInfo;

    private GuiMain() {
        mainFrame = new JFrame();
        mainFrame.setTitle(Window.TITLE);
        mainFrame.setSize(Window.WIDTH, Window.HEIGHT);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        setupIntroPanel();
        mainFrame.setContentPane(introPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);

        String path = "music/boring.wav";
        try {
            audioClip = AudioSystem.getClip();
        } catch (Exception e) {
            throw new RuntimeException("can't get clip");
        }
        playMusic(audioClip, path);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GuiMain::new);
    }

    private void setupIntroPanel() {
        introPanel = new WindowIntroduction();
        introPanel.addButtonListener(WindowIntroduction.Button.LocalPlayButton, ActionListener -> {
            setupLocalGameModesPanel();
            mainFrame.remove(introPanel);
            mainFrame.setContentPane(localGameModesPanel);
            mainFrame.pack();
            mainFrame.revalidate();
        });
        introPanel.addButtonListener(WindowIntroduction.Button.OnlinePlayButton, ActionListener -> {
            setupConnectionOptionsPanel();
            mainFrame.remove(introPanel);
            mainFrame.setContentPane(connectionOptionsPanel);
            mainFrame.pack();
            mainFrame.revalidate();
        });
        introPanel.addButtonListener(WindowIntroduction.Button.RulesButton, ActionListener -> {
            setupRulesPanel();
            mainFrame.remove(introPanel);
            mainFrame.setContentPane(rulesPanel);
            mainFrame.pack();
            mainFrame.revalidate();
        });
        introPanel.addButtonListener(WindowIntroduction.Button.VoiceButton, ActionListener -> {
            volumeMute = !volumeMute;
            introPanel.updateVoice(volumeMute);
            if (audioClip.isActive()) {
                audioClip.stop();
            } else {
                audioClip.start();
            }
            mainFrame.revalidate();
        });
    }

    private void setupLocalGameModesPanel() {
        localGameModesPanel = new WindowLocalGameModes();
        localGameModesPanel.addButtonListener(WindowLocalGameModes.Button.BackButton, ActionListener -> {
            mainFrame.remove(localGameModesPanel);
            mainFrame.setContentPane(introPanel);
            mainFrame.pack();
            mainFrame.revalidate();
        });
        localGameModesPanel.addButtonListener(WindowLocalGameModes.Button.PlayButton, ActionListener -> {
            String mode = localGameModesPanel.getMode();
            if (!mode.isEmpty()) {
                if (mode.equals(WindowLocalGameModes.LOCAL_PLAY_STRING)) {
                    aiPlayer = null;
                } else {
                    aiPlayer = Player.PlayerTwo;
                    try {
                        aiManager = new AiManager(aiPlayer);
                        aiDifficulty = Difficulty.parseDifficulty(mode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            setupOptionsPanel(false);
            mainFrame.remove(localGameModesPanel);
            mainFrame.setContentPane(optionsPanel);
            mainFrame.pack();
            mainFrame.revalidate();
        });
    }

    private void setupOptionsPanel(boolean isServer) {
        optionsPanel = new WindowGameOptions(Options.MINIMUM_WELLS, Options.MAXIMUM_WELLS, isServer);
        optionsPanel.addButtonListener(WindowGameOptions.Button.BackButton, ActionListener -> {
            mainFrame.remove(optionsPanel);
            Window previousPanel = isServer ? hostServerPanel : localGameModesPanel;
            mainFrame.setContentPane(previousPanel);
            mainFrame.pack();
            mainFrame.revalidate();
        });
        optionsPanel.addButtonListener(WindowGameOptions.Button.PlayButton, ActionListener -> {
            Options options = new Options();

            options.setPieRule(optionsPanel.isPieRuleChecked());
            options.setRandomDistribution(optionsPanel.isRandomDistributionChecked());
            options.setNumWells(optionsPanel.getNumWells());
            options.setNumMarbles(optionsPanel.getAverageMarblesPerWell());

            if (isServer) {
                Integer timeLimit;
                try {
                    timeLimit = Integer.parseInt(optionsPanel.getTimeLimit());
                } catch (Exception e) {
                    optionsPanel.setTimeLimitErrorMessage("Time limit must be an integer");
                    return;
                }

                optionsPanel.setTimeLimitErrorMessage(null);

                serverGameInfo = new Info(
                    options.getNumWells(),
                    options.getNumMarbles(),
                    options.isRandomDistribution(),
                    timeLimit);

                setupServerClientModesPanel(true);
                mainFrame.remove(optionsPanel);
                mainFrame.setContentPane(serverClientModesPanel);
                mainFrame.pack();
                mainFrame.revalidate();
            } else {
                localGameOptions = options;

                setupGamePanel();
                mainFrame.remove(optionsPanel);
                mainFrame.setContentPane(gamePanel);
                mainFrame.pack();
                mainFrame.revalidate();
            }
        });
    }

    private void setupConnectionOptionsPanel() {
        connectionOptionsPanel = new WindowConnectionOptions();
        connectionOptionsPanel.addButtonListener(WindowConnectionOptions.Button.BackButton, ActionListener -> {
            mainFrame.remove(connectionOptionsPanel);
            mainFrame.setContentPane(introPanel);
            mainFrame.pack();
            mainFrame.revalidate();
        });
        connectionOptionsPanel.addButtonListener(WindowConnectionOptions.Button.ConnectToServerButton, ActionListener -> {
            setupConnectToServerPanel();
            mainFrame.remove(connectionOptionsPanel);
            mainFrame.setContentPane(connectToServerPanel);
            mainFrame.pack();
            mainFrame.revalidate();
        });
        connectionOptionsPanel.addButtonListener(WindowConnectionOptions.Button.HostServerButton, ActionListener -> {
            setupHostServerPanel();
            mainFrame.remove(connectionOptionsPanel);
            mainFrame.setContentPane(hostServerPanel);
            mainFrame.pack();
            mainFrame.revalidate();
        });
    }

    private void setupConnectToServerPanel() {
        connectToServerPanel = new WindowConnections(false);
        bindConnectionsBackButton(connectToServerPanel);

        connectToServerPanel.addButtonListener(WindowConnections.Button.NextButton, ActionListener -> {
            String ipAddress = connectToServerPanel.getIpAddress();
            String portNumber = connectToServerPanel.getPortNumber();

            try {
                connectionInformation = new ConnectionInformation(ipAddress, Integer.parseInt(portNumber));
                setupServerClientModesPanel(false);
                mainFrame.remove(connectToServerPanel);
                mainFrame.setContentPane(serverClientModesPanel);
                mainFrame.pack();
                mainFrame.revalidate();
            } catch (NumberFormatException e) {
                connectToServerPanel.setErrorMessage("Port number must be an integer");
            }
        });
    }

    private void setupHostServerPanel() {
        try {
            String ipAddress = InetAddress.getLocalHost().getHostAddress();

            hostServerPanel = new WindowConnections(true);
            bindConnectionsBackButton(hostServerPanel);

            hostServerPanel.addButtonListener(WindowConnections.Button.NextButton, ActionListener -> {
                String portNumber = hostServerPanel.getPortNumber();

                try {
                    connectionInformation = new ConnectionInformation(ipAddress, Integer.parseInt(portNumber));
                    setupOptionsPanel(true);

                    mainFrame.remove(hostServerPanel);
                    mainFrame.setContentPane(optionsPanel);
                    mainFrame.pack();
                    mainFrame.revalidate();
                } catch (NumberFormatException e) {
                    hostServerPanel.setErrorMessage("Port number must be an integer");
                }
            });
        } catch (UnknownHostException e) {
            System.out.println("Error unknown host");
        }
    }

    private void setupEndPanel(Player winningPlayer) {
        audioClip.stop();
        audioClip.close();

        Player player = null;
        if (aiPlayer != null) {
            player = aiPlayer.flipPlayer();
        }

        String path;
        if (winningPlayer == player || player == null) {
            path = "music/Kool.wav";
        } else {
            path = "music/defeat.wav";
        }

        playMusic(audioClip, path);

        endPanel = new WindowEnd(player, winningPlayer, true);
        endPanel.addButtonListener(WindowEnd.Button.RestartButton, ActionListener -> {
            audioClip.stop();
            audioClip.close();
            String newPath = "music/boring.wav";
            playMusic(audioClip,newPath);

            mainFrame.remove(endPanel);
            mainFrame.setContentPane(introPanel);
            mainFrame.pack();
            mainFrame.revalidate();
        });
        endPanel.addButtonListener(WindowEnd.Button.VoiceButton, ActionListener -> {
            volumeMute = !volumeMute;
            endPanel.updateVoice(volumeMute);
            if (audioClip.isActive()) {
                audioClip.stop();
            } else {
                audioClip.start();
            }
            mainFrame.revalidate();
        });
    }

    private void setupRulesPanel() {
        rulesPanel = new WindowRules();
        rulesPanel.addButtonListener(WindowRules.Button.BackButton, ActionListener -> {
            mainFrame.remove(rulesPanel);
            mainFrame.setContentPane(introPanel);
            mainFrame.pack();
            mainFrame.revalidate();
        });
    }

    private void bindConnectionsBackButton(WindowConnections window) {
        window.addButtonListener(WindowConnections.Button.BackButton, ActionListener -> {
            window.setErrorMessage(null);
            mainFrame.remove(window);
            mainFrame.setContentPane(connectionOptionsPanel);
            mainFrame.pack();
            mainFrame.revalidate();
        });
    }

    private void setupServerClientModesPanel(boolean isServer) {
        serverClientModesPanel = new WindowServerClientModes(isServer);
        serverClientModesPanel.addButtonListener(WindowServerClientModes.Button.BackButton, ActionListener -> {
            mainFrame.remove(serverClientModesPanel);
            mainFrame.setContentPane(isServer ? optionsPanel : connectToServerPanel);
            mainFrame.pack();
            mainFrame.revalidate();
        });
        serverClientModesPanel.addButtonListener(WindowServerClientModes.Button.SpawnAiButton, ActionListener -> {
            if (isServer) {
                try {
                    connectionInformation.setServerAddress(InetAddress.getLocalHost().getHostAddress());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    return;
                }

                new Thread(() -> {
                    try {
                        new ConsoleServer(connectionInformation.getSocketNumber(), serverGameInfo);
                    } catch (InvalidCommandException e) {
                        e.printStackTrace();
                    }
                }).start();
            }

            new Thread(() -> {
                while (true) {
                    try {
                        new GuiAiClient(connectionInformation);
                        return;
                    } catch (IOException | InvalidCommandException ignored) {
                    }
                }
            }).start();

            mainFrame.dispose();
        });
        serverClientModesPanel.addButtonListener(WindowServerClientModes.Button.PlayAsPlayerButton, ActionListener -> {
            if (isServer) {
                new Thread(() -> {
                    try {
                        new ConsoleServer(connectionInformation.getSocketNumber(), serverGameInfo);
                    } catch (InvalidCommandException e) {
                        e.printStackTrace();
                    }
                }).start();
            }

            new Thread(() -> {
                while (true) {
                    try {
                        new GuiPlayerClient(connectionInformation);
                        return;
                    } catch (IOException | InvalidCommandException ignored) {
                    }
                }
            }).start();

            mainFrame.dispose();
        });
        serverClientModesPanel.addButtonListener(WindowServerClientModes.Button.SpectateButton, ActionListener -> {
            new Thread(() -> {
                try {
                    new GuiServer(connectionInformation.getSocketNumber(), serverGameInfo);
                } catch (InvalidCommandException e) {
                    e.printStackTrace();
                }
            }).start();

            mainFrame.dispose();
        });
    }

    private void setupGamePanel() {
        gameManager = new GameManager(localGameOptions);
        gameManager.printBoardState();

        audioClip.stop();
        audioClip.close();
        String path = "music/Hollow_Knight.wav";
        playMusic(audioClip, path);

        gamePanel = new WindowGame(gameManager, null);

        WindowGameButtonSetupUtils gameButtonSetup;

        gameButtonSetup = new WindowGameButtonSetupUtils(gamePanel, gameManager);
        Player user = aiPlayer != null ? aiPlayer.flipPlayer() : null;

        gameButtonSetup.setupLeaveButton(mainFrame, introPanel);
        gameButtonSetup.setupPieRuleButton(user, this::makeMove);
        gameButtonSetup.setupSubmitButton(user, this::makeMove);
        gameButtonSetup.setupMouseListener(user, this::makeMove);

        gamePanel.setButtonEnabled(WindowGame.Button.LeaveButton, true);
        gamePanel.addButtonListener(WindowGame.Button.VoiceButton, ActionListener -> {
            volumeMute = !volumeMute;
            gamePanel.updateVoice(volumeMute);
            if(audioClip.isActive()) {
                audioClip.stop();
            }
            else {
                audioClip.start();
            }
            mainFrame.revalidate();
        });
    }

    private Void makeMove(Move move) {
        Player activePlayer = gameManager.getActivePlayer();
        MoveType moveType = gameManager.makeMove(move);
        System.out.println(activePlayer + " played: " + move);

        GameManager.printMessageBasedOnMoveType(moveType, activePlayer);
        if (moveType == MoveType.IllegalMove) {
            return null;
        }

        gamePanel.resetMoveField();
        gamePanel.setMoveFieldErrorMessage(null);
        gamePanel.setButtonEnabled(WindowGame.Button.PieRuleButton, gameManager.getGameState() == GameState.PieEligible);
        gamePanel.paintImmediately(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
        gameManager.printBoardState();

        if (gameManager.getGameState() == GameState.GameOver) {
            gamePanel.repaint();
            GameManager.printPostGameResults(gameManager.getWinner(), gameManager);

            setupEndPanel(gameManager.getWinner());
            mainFrame.remove(gamePanel);
            mainFrame.setContentPane(endPanel);
            mainFrame.pack();
            mainFrame.revalidate();
            return null;
        }

        if (aiPlayer != null) {
            // make ai moves if the userPlayer was userPlayer 1 and is now userPlayer 2
            if (gameManager.getActivePlayer() == activePlayer.flipPlayer() &&
                gameManager.getActivePlayer() == aiPlayer) {

                // make ai moves
                aiManager.setBoard(gameManager.getBoard(), gameManager.getActivePlayer().flipPlayer());
                List<Move> aiMoves = aiManager.go(aiDifficulty, Options.DEFAULT_LOCAL_AI_MOVE_TIME);

                for (Move aiMove : aiMoves) {
                    makeMove(aiMove);
                }
            }
        }

        return null;
    }

    private void playMusic(Clip audioClip, String path) {
        try {
            File input = new File(path);
            audioClip.open(AudioSystem.getAudioInputStream(input));
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            throw new RuntimeException("Can't open music file\n");
        }
    }
}
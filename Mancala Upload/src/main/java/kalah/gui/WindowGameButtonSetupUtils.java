package kalah.gui;

import kalah.gamemanager.GameManager;
import kalah.gamemanager.GameState;
import kalah.gamemanager.Move;
import kalah.gamemanager.Player;
import kalah.gamemanager.UserMoveType;

import javax.swing.JFrame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Function;

class WindowGameButtonSetupUtils {
    private WindowGame gamePanel;
    private GameManager gameManager;

    WindowGameButtonSetupUtils(
        WindowGame gamePanel,
        GameManager gameManager) {

        this.gamePanel = gamePanel;
        this.gameManager = gameManager;
    }

    void setupLeaveButton(JFrame mainFrame, Window targetPanel) {
        gamePanel.setButtonEnabled(WindowGame.Button.LeaveButton, true);

        gamePanel.addButtonListener(WindowGame.Button.LeaveButton, ActionListener -> {
            mainFrame.remove(gamePanel);
            mainFrame.setContentPane(targetPanel);
            mainFrame.pack();
            mainFrame.revalidate();
        });
    }

    void setupPieRuleButton(Player user, Function<Move, Void> makeMoveFunction) {
        gamePanel.setButtonEnabled(WindowGame.Button.PieRuleButton, gameManager.getGameState() == GameState.PieEligible);
        gamePanel.addButtonListener(WindowGame.Button.PieRuleButton, ActionListener -> {
            if (gameManager.getActivePlayer() == user || user == null) {
                if (gameManager.getGameState() == GameState.PieEligible) {
                    makeMoveFunction.apply(new Move(UserMoveType.PieMove, null));
                    gamePanel.setButtonEnabled(WindowGame.Button.PieRuleButton, gameManager.getGameState() == GameState.PieEligible);
                    gamePanel.repaint();
                } else {
                    gamePanel.setButtonEnabled(WindowGame.Button.PieRuleButton, false);
                }
            }
        });
    }

    void setupSubmitButton(Player user, Function<Move, Void> makeMoveFunction) {
        gamePanel.setButtonEnabled(WindowGame.Button.SubmitButton, true);
        gamePanel.addButtonListener(WindowGame.Button.SubmitButton,
            ActionListener -> {
                if (gameManager.getActivePlayer() == user || user == null) {
                    makeMove(gamePanel.getMoveFromField(), makeMoveFunction);
                }
            });
    }

    void setupMouseListener(Player user, Function<Move, Void> makeMoveFunction) {
        gamePanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameManager.getActivePlayer() == user || user == null) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        try {
                            int wellNumber = gamePanel.getWellNumber(e.getX(), e.getY(), gameManager.getActivePlayer());
                            makeMove(String.valueOf(wellNumber), makeMoveFunction);
                        } catch (ClickNotInWellException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void makeMove(String input, Function<Move, Void> makeMoveFunction) {
        try {
            int wellNumber = Integer.parseInt(input);
            Integer move = --wellNumber;
            makeMoveFunction.apply(new Move(UserMoveType.WellMove, move));
        } catch (NumberFormatException e) {
            gamePanel.setMoveFieldErrorMessage("Move must be an integer");
        }
    }
}

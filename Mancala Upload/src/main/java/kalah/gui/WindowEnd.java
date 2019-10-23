package kalah.gui;

import kalah.gamemanager.Player;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Map;

import static java.util.Map.entry;

public class WindowEnd extends Window<WindowEnd.Button> {
    private static final int PADDING = 10;
    private JButton restartButton = new JButton();
    private JButton voiceButton = new JButton();
    private JPanel west = new JPanel();
    private JPanel east = new JPanel();
    private JPanel center = new JPanel();
    private Image voiceIcon;
    private Player winningPlayer;
    private Player player;
    private boolean isLocal;

    WindowEnd(Player player, Player winningPlayer, boolean isLocal) {
        this.player = player;
        this.winningPlayer = winningPlayer;
        this.isLocal = isLocal;

        initGui();
    }

    @Override
    protected void initGui() {
        BorderLayout borderLayout = new BorderLayout();
        this.setLayout(borderLayout);
        borderLayout.setHgap(50);

        restartButton = new JButton("Restart");
        restartButton.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
        Dimension buttonSize = restartButton.getPreferredSize();

        if (isLocal) {
            west.add(restartButton, BorderLayout.PAGE_START);
            add(west, BorderLayout.WEST);
        }

        String path = "img/resizedUnmute.png";
        voiceIcon = getImage(path);
        voiceButton = new JButton();
        voiceButton.setIcon(new ImageIcon(voiceIcon));
        voiceButton.setToolTipText("Mute Music");
        voiceButton.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
        voiceButton.setPreferredSize(buttonSize);

        east.add(voiceButton, BorderLayout.PAGE_START);
        add(east, BorderLayout.EAST);

        // show winning player
        JLabel endText;
        if (winningPlayer == null) {
            endText = new JLabel("It's a TIE!");
        } else {
            if (player == null) {
                endText = new JLabel(winningPlayer.toString() + " won");
            } else {
                if (winningPlayer == player) {
                    endText = new JLabel("You won!");
                } else {
                    endText = new JLabel("You lose!");
                    endText.setToolTipText("Better Luck Next Time");
                }
            }
        }

        Font endResult = new Font("Arial", Font.PLAIN, 32);
        endText.setFont(endResult);
        endText.setForeground(Color.red);
        endText.setHorizontalAlignment(JLabel.CENTER);
        endText.setVerticalAlignment(JLabel.CENTER);
        add(endText, BorderLayout.CENTER);

        //Testing Functions
        //east.setBorder(BorderFactory.createLineBorder(Color.black));
        //west.setBorder(BorderFactory.createLineBorder(Color.black));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        String path;
        if (player == null || winningPlayer == player) {
            path = "img/resizedSimpsons.jpg";
        } else {
            path = "img/resizedChessDefeat.jpg";
        }
        Image endScreen;
        endScreen = getImage(path);
        super.paintComponent(g);

        int windowWidth = this.getPreferredSize().width;
        int imageWidth = endScreen.getWidth(null);
        int startingX = (windowWidth - imageWidth) / 2;

        int windowHeight = this.getPreferredSize().height;
        int imageHeight = endScreen.getHeight(null);
        int startingY = (windowHeight - imageHeight) / 2;

        g.drawImage(endScreen, startingX, startingY, null);
    }

    @Override
    protected Map<Button, JButton> getButtonMap() {
        return Map.ofEntries(
            entry(Button.RestartButton, restartButton),
            entry(Button.VoiceButton, voiceButton)
        );
    }

    public void updateVoice(boolean b) {

        String path;

        if (b) {
            path = "img/resizedMute.png";
            voiceButton.setToolTipText("UnMute Music");
        } else {
            path = "img/resizedUnmute.png";
            voiceButton.setToolTipText("Mute Music");
        }

        voiceIcon = getImage(path);
        voiceButton.setIcon(new ImageIcon(voiceIcon));
    }

    public enum Button {
        RestartButton,
        VoiceButton
    }
}

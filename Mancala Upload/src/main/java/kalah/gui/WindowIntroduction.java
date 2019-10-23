package kalah.gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.Map;

import static java.util.Map.entry;

public class WindowIntroduction extends Window<WindowIntroduction.Button> {
    private static JButton localPlayButton = new JButton();
    private static JButton onlinePlayButton = new JButton();
    private static JButton rulesButton = new JButton();
    private static JButton voiceButton = new JButton();
    private Image voiceIcon;

    WindowIntroduction() {
        initGui();
    }

    @Override
    protected void initGui() {

        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = Window.generateGridBagConstraints();

        JLabel title = new JLabel(Window.TITLE);
        title.setFont(new Font(Window.FONT_NAME, Window.FONT_STYLE, 32));
        title.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(title, gbc);

        localPlayButton = new JButton("Local Play");
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(localPlayButton, gbc);

        onlinePlayButton = new JButton("Online Play");
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(onlinePlayButton, gbc);

        rulesButton = new JButton("Rules");
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(rulesButton, gbc);

        voiceButton = new JButton();
        String path = "img/resizedUnmute.png";
        Image voiceIcon = getImage(path);
        voiceButton.setIcon(new ImageIcon(voiceIcon));
        voiceButton.setToolTipText("Mute Music");
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        add(voiceButton,gbc);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        String path = "img/hmmm.jpg";
        Image image = getImage(path);
        g.drawImage(image, 0, 0, null);
    }

    @Override
    protected Map<Button, JButton> getButtonMap() {
        return Map.ofEntries(
            entry(Button.LocalPlayButton, localPlayButton),
            entry(Button.OnlinePlayButton, onlinePlayButton),
            entry(Button.RulesButton, rulesButton),
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
        LocalPlayButton,
        OnlinePlayButton,
        RulesButton,
        VoiceButton
    }
}

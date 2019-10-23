package kalah.gui;

import kalah.ai.Difficulty;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class WindowLocalGameModes extends Window<WindowLocalGameModes.Button> {
    static final String LOCAL_PLAY_STRING = "Local Play";
    private JButton backButton = new JButton();
    private JButton playButton = new JButton();
    private ButtonGroup gameModes = new ButtonGroup();
    private List<JRadioButton> radioButtons = new ArrayList<>();

    WindowLocalGameModes() {
        initGui();
    }

    String getMode() {
        for (JRadioButton button : radioButtons) {
            if (button.isSelected()) {
                return button.getText();
            }
        }

        throw new RuntimeException("No mode selected");
    }

    @Override
    protected void initGui() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = generateGridBagConstraints();

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;

        backButton = new JButton("Back");
        add(backButton, gbc);

        int numItems = 0;
        int currentColumn = 1;
        final int MAX_IN_COLUMN = 3;

        JRadioButton localPlay = new JRadioButton(LOCAL_PLAY_STRING);
        localPlay.setSelected(true);
        gbc.gridx = currentColumn;
        add(localPlay, gbc);
        gameModes.add(localPlay);
        radioButtons.add(localPlay);

        for (Difficulty difficulty : Difficulty.values()) {
            JRadioButton radioButton = new JRadioButton(difficulty.toString());
            numItems = (numItems + 1) % MAX_IN_COLUMN;

            if (numItems == 0) {
                ++currentColumn;
            }

            gbc.gridx = currentColumn;
            add(radioButton, gbc);

            gameModes.add(radioButton);
            radioButtons.add(radioButton);
        }

        gbc.gridx = currentColumn + 1;
        playButton = new JButton("Play");
        add(playButton, gbc);
    }

    @Override
    protected Map<Button, JButton> getButtonMap() {
        return Map.ofEntries(
            entry(Button.BackButton, backButton),
            entry(Button.PlayButton, playButton)
        );
    }

    public enum Button {
        BackButton,
        PlayButton
    }
}

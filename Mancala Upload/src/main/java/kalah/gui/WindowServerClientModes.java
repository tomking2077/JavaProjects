package kalah.gui;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Map;

import static java.util.Map.entry;

class WindowServerClientModes extends Window<WindowServerClientModes.Button> {
    private JButton backButton = new JButton();
    private JButton spawnAiButton = new JButton();
    private JButton playAsPlayerButton = new JButton();
    private JButton spectateButton = new JButton();

    private JLabel errorMessage = new JLabel();

    private boolean includeSpectateButton;

    WindowServerClientModes(boolean includeSpectateButton) {
        this.includeSpectateButton = includeSpectateButton;
        initGui();
    }

    void setErrorMessage(String message) {
        errorMessage.setOpaque(true);
        errorMessage.setText(message);
    }

    @Override
    protected void initGui() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = Window.generateGridBagConstraints();

        gbc.gridx = 0;
        backButton = new JButton("Back");
        add(backButton, gbc);

        gbc.gridx = 1;
        spawnAiButton = new JButton("Spawn Ai");
        add(spawnAiButton, gbc);

        playAsPlayerButton = new JButton("Play as Player");
        add(playAsPlayerButton, gbc);

        if (includeSpectateButton) {
            spectateButton = new JButton("Spectate");
            add(spectateButton, gbc);
        }

        errorMessage = Window.generateErrorMessageLabel();
        add(errorMessage, gbc);
    }

    @Override
    protected Map<Button, JButton> getButtonMap() {
        return Map.ofEntries(
            entry(Button.BackButton, backButton),
            entry(Button.SpawnAiButton, spawnAiButton),
            entry(Button.PlayAsPlayerButton, playAsPlayerButton),
            entry(Button.SpectateButton, spectateButton)
        );
    }

    public enum Button {
        BackButton,
        SpawnAiButton,
        PlayAsPlayerButton,
        SpectateButton
    }
}

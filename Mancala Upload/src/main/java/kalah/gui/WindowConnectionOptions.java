package kalah.gui;

import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Map;

import static java.util.Map.entry;

public class WindowConnectionOptions extends Window<WindowConnectionOptions.Button> {
    private static JButton backButton = new JButton();
    private static JButton connectToServerButton = new JButton();
    private static JButton hostServerButton = new JButton();

    WindowConnectionOptions() {
        initGui();
    }

    @Override
    protected void initGui() {
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = Window.generateGridBagConstraints();

        backButton = new JButton("Back");
        gbc.gridx = 0;
        add(backButton, gbc);

        connectToServerButton = new JButton("Connect to Server");
        gbc.gridx = 1;
        add(connectToServerButton, gbc);

        hostServerButton = new JButton("Host Server");
        gbc.gridx = 1;
        add(hostServerButton, gbc);
    }

    @Override
    protected Map<Button, JButton> getButtonMap() {
        return Map.ofEntries(
            entry(Button.BackButton, backButton),
            entry(Button.ConnectToServerButton, connectToServerButton),
            entry(Button.HostServerButton, hostServerButton)
        );
    }

    public enum Button {
        BackButton,
        ConnectToServerButton,
        HostServerButton
    }
}

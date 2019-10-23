package kalah.gui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Map;

import static java.util.Map.entry;

public class WindowConnections extends Window<WindowConnections.Button> {
    private static JButton backButton = new JButton();
    private static JButton nextButton = new JButton();

    private static JTextField ipAddressField = new JTextField();
    private static JTextField portNumberField = new JTextField();

    private static JLabel errorMessage = new JLabel();

    private boolean isServer;

    WindowConnections(boolean isServer) {
        this.isServer = isServer;
        initGui();
    }

    String getIpAddress() {
        return ipAddressField.getText();
    }

    String getPortNumber() {
        return portNumberField.getText();
    }

    void setErrorMessage(String message) {
        errorMessage.setOpaque(true);
        errorMessage.setText(message);
    }

    @Override
    protected void initGui() {
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = Window.generateGridBagConstraints();

        backButton = new JButton("Back");
        gbc.gridx = 0;
        add(backButton, gbc);

        gbc.anchor = GridBagConstraints.WEST;

        if (!isServer) {
            JLabel ipAddressLabel = new JLabel("IP Address:");
            gbc.gridx = 1;
            add(ipAddressLabel, gbc);

            gbc.gridx = 2;
            int numOfDigitsInIpAddress = 15;
            ipAddressField = new JTextField(numOfDigitsInIpAddress);
            add(ipAddressField, gbc);
        }

        JLabel portNumberLabel = new JLabel("Port:");
        gbc.gridx = 1;
        add(portNumberLabel, gbc);

        int numOfDigitsInPort = 5;
        portNumberField = new JTextField(numOfDigitsInPort);
        gbc.gridx = 2;
        add(portNumberField, gbc);

        gbc.gridx = 1;
        nextButton = new JButton("Next");
        add(nextButton, gbc);

        errorMessage = Window.generateErrorMessageLabel();
        add(errorMessage, gbc);
    }

    @Override
    protected Map<Button, JButton> getButtonMap() {
        return Map.ofEntries(
            entry(Button.BackButton, backButton),
            entry(Button.NextButton, nextButton)
        );
    }

    public enum Button {
        BackButton,
        NextButton
    }
}

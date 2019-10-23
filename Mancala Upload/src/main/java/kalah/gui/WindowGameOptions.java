package kalah.gui;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;

import static java.util.Map.entry;

public class WindowGameOptions extends Window<WindowGameOptions.Button> {
    private JButton backButton = new JButton();
    private JButton playButton = new JButton();

    private JCheckBox pieRuleCheckbox = new JCheckBox();
    private JCheckBox randomDistributionCheckbox = new JCheckBox();

    private JTextField timeLimitField = new JTextField();
    private JLabel timeLimitErrorMessage = new JLabel();

    private JComboBox<Integer> wellsList;
    private JComboBox<Integer> avgMarblesList;

    private boolean includeTimeLimit;

    WindowGameOptions(int minWells, int maxWells, boolean includeTimeLimit) {
        if (maxWells < minWells) {
            throw new RuntimeException("MaxWells cant be larger than MinWells");
        }

        int numWells = maxWells - minWells + 1;
        Integer possibleWells[] = new Integer[numWells];
        for (int i = 0; i < numWells; ++i) {
            possibleWells[i] = minWells + i;
        }
        wellsList = new JComboBox<>(possibleWells);

        avgMarblesList = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9});

        this.includeTimeLimit = includeTimeLimit;

        initGui();
    }

    boolean isPieRuleChecked() {
        return pieRuleCheckbox.isSelected();
    }

    boolean isRandomDistributionChecked() {
        return randomDistributionCheckbox.isSelected();
    }

    Integer getNumWells() {
        return (Integer) wellsList.getSelectedItem();
    }

    String getTimeLimit() {
        return timeLimitField.getText();
    }

    void setTimeLimitErrorMessage(String message) {
        timeLimitErrorMessage.setOpaque(true);
        timeLimitErrorMessage.setText(message);
    }

    Integer getAverageMarblesPerWell() {
        return (Integer) avgMarblesList.getSelectedItem();
    }

    @Override
    protected void initGui() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = Window.generateGridBagConstraints();

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;

        backButton = new JButton("Back");
        add(backButton, gbc);

        JLabel numWellsLabel = new JLabel("Number of Wells");
        gbc.gridx = 1;
        add(numWellsLabel, gbc);

        gbc.gridx = 2;
        add(wellsList, gbc);

        JLabel averageMarblesPerWellLabel = new JLabel("Avg Marbles per Well");
        gbc.gridx = 1;
        add(averageMarblesPerWellLabel, gbc);

        gbc.gridx = 2;
        add(avgMarblesList, gbc);

        if (includeTimeLimit) {
            JLabel timeLimitLabel = new JLabel("Time Limit (ms)");
            gbc.gridx = 1;
            add(timeLimitLabel, gbc);

            int timeLimitColumns = 5;
            timeLimitField = new JTextField(timeLimitColumns);
            gbc.gridx = 2;
            add(timeLimitField, gbc);

            gbc.insets = new Insets(0, 0, 0, 0);
            timeLimitErrorMessage = Window.generateErrorMessageLabel();
            add(timeLimitErrorMessage, gbc);
            gbc.insets = Window.INSETS;

            gbc.gridx = 1;
            JLabel emptyLabel = new JLabel();
            add(emptyLabel, gbc);
        }


        pieRuleCheckbox = new JCheckBox("Pie Rule");
        add(pieRuleCheckbox, gbc);

        randomDistributionCheckbox = new JCheckBox("Randomly Distribute Marbles");
        add(randomDistributionCheckbox, gbc);

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

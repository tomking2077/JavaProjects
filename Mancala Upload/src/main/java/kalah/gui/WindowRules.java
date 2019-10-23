package kalah.gui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Map;

import static java.util.Map.entry;

public class WindowRules extends Window<WindowRules.Button> {
    private JButton backButton = new JButton();
    private JTextArea rulesText = new JTextArea();
    private JScrollPane scrollText = new JScrollPane();
    private JPanel west = new JPanel();
    private JLabel rulesTitle = new JLabel();

    WindowRules() {
        initGui();
    }

    @Override
    protected void initGui() {
        BorderLayout borderLayout = new BorderLayout();
        this.setLayout(borderLayout);
        borderLayout.setHgap(50);

        backButton = new JButton("Back");
        backButton.setBorder(new EmptyBorder(10, 20, 10, 20));


        west = new JPanel(new BorderLayout());
        west.add(backButton, BorderLayout.PAGE_START);

        add(west, BorderLayout.WEST);

        //reference : https://www.thespruce.com/how-to-play-mancala-409424
        rulesText = new JTextArea(
            "The Mancala board is made up of two rows of six holes, or pits, each.\n\n " +
                "Four pieces—marbles, chips, or stones—are placed in each of the 12 holes. The color of the pieces is irrelevant.\n\n " +
                "Each player has a store (called a Mancala) to the right side of the Mancala board.\n\n " +
                "The game begins with one player picking up all of the pieces in any one of the holes on his side.\n\n " +
                "Moving counter-clockwise, the player deposits one of the stones in each hole until the stones run out.\n\n " +
                "If you run into your own store, deposit one piece in it. If you run into your opponent's store, skip it.\n\n " +
                "If the last piece you drop is in your own store, you get a free turn.\n\n " +
                "If the last piece you drop is in an empty hole on your side, you capture that piece and any pieces in the hole directly opposite.\n\n " +
                "Always place all captured pieces in your store.\n\n " +
                "The game ends when all six spaces on one side of the Mancala board are empty.\n\n " +
                "The player who still has pieces on his side of the board when the game ends capture all of those pieces.\n\n " +
                "Count all the pieces in each store. The winner is the player with the most pieces."
        );
        rulesText.setFont(new Font("Serif", Font.ITALIC, 16));
        rulesText.setLineWrap(true);
        rulesText.setWrapStyleWord(true);
        rulesText.setEditable(false);

        scrollText = new JScrollPane(rulesText);
        scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollText.setPreferredSize(new Dimension(250, 250));
        scrollText.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(scrollText, BorderLayout.CENTER);

        rulesTitle = new JLabel("Rules");
        rulesTitle.setForeground(Color.black);
        Font endResult = new Font("Helvetica", Font.PLAIN, 32);
        rulesTitle.setFont(endResult);
        rulesTitle.setHorizontalAlignment(JLabel.CENTER);
        rulesTitle.setVerticalAlignment(JLabel.CENTER);

        add(rulesTitle, BorderLayout.NORTH);
    }

    @Override
    protected Map<Button, JButton> getButtonMap() {
        return Map.ofEntries(
            entry(Button.BackButton, backButton)
        );
    }

    public enum Button {
        BackButton
    }
}

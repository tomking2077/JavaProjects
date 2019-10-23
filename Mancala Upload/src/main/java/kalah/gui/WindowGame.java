package kalah.gui;

import kalah.gamemanager.Board;
import kalah.gamemanager.GameManager;
import kalah.gamemanager.Player;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.util.Map.entry;

public class WindowGame extends Window<WindowGame.Button> {
    // colors
    private static final String BOARD_COLOR = "#af9061";
    private static final String WELL_COLOR = "#876c44";
    private static final String TEXT_COLOR = "#656565";
    private static final int PADDING = 25;
    private static final int WELL_DIAMETER = 100;
    private static final int MARBLE_DIAMETER = 15;
    private static final int HOUSE_HEIGHT = 250;
    private static final int HOUSE_WIDTH = 100;
    private static final int HOUSE_ROUNDED_CORNER = 50;
    private static final int WIDTH_PER_WELL = WELL_DIAMETER + PADDING;
    private static final int BOARD_HEIGHT = 400;
    private static final int BOARD_ROUNDED_CORNER = 30;
    private static final int BUTTON_HEIGHT = 30;
    private static final int BUTTON_WIDTH = 100;
    private static final int TEXT_FIELD_WIDTH = 50;
    private static final int TEXT_FIELD_HEIGHT = 30;
    private static final int TEXT_FIELD_ERROR_WIDTH = 200;
    private static final int TEXT_FIELD_ERROR_HEIGHT = 20;
    private static final int WELL_MARBLES_ROWS = 3;
    private static final int WELL_MARBLES_COLUMNS = 3;
    private static final int marbleSpacingX = (WELL_DIAMETER - 90) / WELL_MARBLES_ROWS;
    private static final int marbleSpacingY = (WELL_DIAMETER - 90) / WELL_MARBLES_COLUMNS;
    private static final int HOUSE_MARBLES_ROWS = 12;
    private static final int HOUSE_MARBLES_COLUMNS = 3;
    private static final Font LABEL_FONT = new Font(Window.FONT_NAME, Window.FONT_STYLE, 32);
    private JButton leaveButton = new JButton();
    private JButton submitMoveFromTextButton = new JButton();
    private JButton pieRuleButton = new JButton();
    private JButton voiceButton = new JButton();
    private JTextField moveField = new JTextField();
    private JLabel moveFieldErrorMessage = new JLabel();
    private JLabel activePlayerLabel = new JLabel();
    private Board board;
    private GameManager gameManager;
    private List<Ellipse2D> wells1;
    private List<Ellipse2D> wells2;
    private Long timePerTurn = null;
    // dimensions constants
    private int windowWidth = 0;
    private int windowHeight = 0;
    private int boardWidth;
    private boolean setupWells1[];
    private boolean setupWells2[];

    WindowGame(GameManager gameManager, Long millisPerTurn) {
        this.gameManager = gameManager;
        this.board = gameManager.getBoard();
        this.wells1 = Arrays.asList(new Ellipse2D.Double[board.getNumWells()]);
        this.wells2 = Arrays.asList(new Ellipse2D.Double[board.getNumWells()]);
        this.timePerTurn = millisPerTurn;

        initGui();
    }

    String getMoveFromField() {
        return moveField.getText();
    }

    void resetMoveField() {
        moveField.setText(null);
    }

    void setMoveFieldErrorMessage(String message) {
        moveFieldErrorMessage.setOpaque(true);
        moveFieldErrorMessage.setText(message);
    }

    void setButtonEnabled(Button button, boolean enabled) {
        getButtonMap().get(button).setEnabled(enabled);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(windowWidth, windowHeight);
    }

    @Override
    protected void initGui() {
        this.setLayout(null);
        boardWidth = PADDING + HOUSE_WIDTH + board.getNumWells() * WIDTH_PER_WELL + PADDING + HOUSE_WIDTH + PADDING;
        windowWidth = boardWidth + 2 * PADDING;
        windowHeight = BOARD_HEIGHT + 2 * PADDING + BUTTON_HEIGHT + 2 * PADDING;

        setSize(windowWidth, windowHeight);

        final int submitMoveButtonX = windowWidth - BUTTON_WIDTH - PADDING;

        submitMoveFromTextButton = new JButton("Submit");
        submitMoveFromTextButton.setBounds(submitMoveButtonX, PADDING, BUTTON_WIDTH, BUTTON_HEIGHT);
        this.add(submitMoveFromTextButton);

        final int moveTextFieldX = submitMoveButtonX - PADDING - TEXT_FIELD_WIDTH;

        moveField = new JTextField();
        moveField.setBounds(moveTextFieldX, PADDING, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
        this.add(moveField);

        moveFieldErrorMessage = new JLabel();
        moveFieldErrorMessage.setBounds(moveTextFieldX, 2 * PADDING, TEXT_FIELD_ERROR_WIDTH, TEXT_FIELD_ERROR_HEIGHT);
        moveFieldErrorMessage.setFont(new Font("Arial", Font.PLAIN, 14));
        moveFieldErrorMessage.setForeground(Color.RED);
        this.add(moveFieldErrorMessage);

        final int quitButtonX = PADDING;

        leaveButton = new JButton("Leave");
        leaveButton.setBounds(quitButtonX, PADDING, BUTTON_WIDTH, BUTTON_HEIGHT);
        this.add(leaveButton);

        final int pieRuleButtonX = quitButtonX + BUTTON_WIDTH + PADDING;

        pieRuleButton = new JButton("PIE");
        pieRuleButton.setOpaque(true);
        pieRuleButton.setBounds(pieRuleButtonX, PADDING, BUTTON_WIDTH, BUTTON_HEIGHT);
        this.add(pieRuleButton);

        final int activePlayerIntroLabelX = pieRuleButtonX + BUTTON_WIDTH + PADDING;

        JLabel activePlayerIntroLabel = new JLabel("Active Player:");
        final int activePlayerIntroLabelWidth = 110;
        activePlayerIntroLabel.setBounds(activePlayerIntroLabelX, PADDING, activePlayerIntroLabelWidth, BUTTON_HEIGHT);
        this.add(activePlayerIntroLabel);

        activePlayerLabel = new JLabel(gameManager.getActivePlayer().toString());
        final int activePlayerLabelX = activePlayerIntroLabelX + activePlayerIntroLabelWidth;
        final int activePlayerLabelWidth = 80;
        activePlayerLabel.setBounds(activePlayerLabelX, PADDING, activePlayerLabelWidth, BUTTON_HEIGHT);
        this.add(activePlayerLabel);

        if (timePerTurn != null) {
            JLabel timePerTurnIntroLabel = new JLabel("time/turn (ms):");
            timePerTurnIntroLabel.setBounds(activePlayerIntroLabelX,
                PADDING + activePlayerIntroLabel.getHeight(),
                activePlayerIntroLabelWidth,
                BUTTON_HEIGHT);
            this.add(timePerTurnIntroLabel);

            JLabel timePerTurnLabel = new JLabel(timePerTurn.toString());
            timePerTurnLabel.setBounds(activePlayerLabelX,
                PADDING + activePlayerLabel.getHeight(),
                activePlayerLabelWidth,
                BUTTON_HEIGHT);
            this.add(timePerTurnLabel);
        }

        JLabel moveLabel = new JLabel("Enter Move:");
        final int moveLabelWidth = 100;
        moveLabel.setBounds(moveTextFieldX - moveLabelWidth, PADDING, moveLabelWidth, BUTTON_HEIGHT);
        this.add(moveLabel);

        for (Button button : getButtonMap().keySet()) {
            setButtonEnabled(button, false);
        }

        voiceButton = new JButton();
        voiceButton = new JButton();
        String path = "img/resizedUnmute.png";
        Image voiceIcon = getImage(path);
        voiceButton.setIcon(new ImageIcon(voiceIcon));
        voiceButton.setToolTipText("Mute Music");
        voiceButton.setBounds(leaveButton.getX(), leaveButton.getY() + leaveButton.getHeight() + PADDING, BUTTON_WIDTH/2, BUTTON_HEIGHT);
        this.add(voiceButton);


        setupWells1 = new boolean[board.getNumWells()];
        Arrays.fill(setupWells1, true);
        setupWells2 = new boolean[board.getNumWells()];
        Arrays.fill(setupWells2, true);

        this.validate();
    }

    Integer getWellNumber(int x, int y, Player player) throws ClickNotInWellException {
        List<Ellipse2D> wells = new ArrayList<>();

        switch (player) {
            case PlayerOne:
                wells = wells1;
                break;
            case PlayerTwo:
                wells = wells2;
                break;
        }

        for (int i = 0; i < board.getNumWells(); i++) {
            if (wells.get(i).contains(x, y)) {
                return i + 1;
            }
        }
        throw new ClickNotInWellException("Click not in well");
    }

    @Override
    public void paintComponent(Graphics g) {

        //Added to properly clear trails
        super.paintComponent(g);
        // update player
        activePlayerLabel.setOpaque(true);


        if (gameManager.getActivePlayer() != null) {
            activePlayerLabel.setText(gameManager.getActivePlayer().toString());
        } else {
            activePlayerLabel.setText(null);
        }

        Graphics2D graphics = (Graphics2D) g;

        // draw main board
        final int boardX = (windowWidth - boardWidth - PADDING) / 2;
        final int boardY = (windowHeight - BOARD_HEIGHT) / 2 + BUTTON_HEIGHT + PADDING;
        graphics.setColor(Color.decode(BOARD_COLOR));
        graphics.fillRoundRect(boardX, boardY, boardWidth, BOARD_HEIGHT, BOARD_ROUNDED_CORNER, BOARD_ROUNDED_CORNER);

        graphics.setColor(Color.decode(WELL_COLOR));
        graphics.setFont(LABEL_FONT);

        // draw player 2 house
        final int p2HouseX = boardX + PADDING;
        final int p2HouseY = boardY + PADDING;
        graphics.fillRoundRect(p2HouseX, p2HouseY, HOUSE_WIDTH, HOUSE_HEIGHT, HOUSE_ROUNDED_CORNER, HOUSE_ROUNDED_CORNER);

        graphics.setColor(Color.decode(TEXT_COLOR));
        drawCenteredString(
            graphics,
            String.valueOf(board.getHouseMarbles(Player.PlayerTwo)),
            new Rectangle(
                p2HouseX,
                p2HouseY + HOUSE_HEIGHT + PADDING,
                HOUSE_WIDTH,
                LABEL_FONT.getSize()
            ),
            LABEL_FONT
        );

        //draw player 2 house marbles
        int player2Score = board.getHouseMarbles(Player.PlayerTwo);
        int currentHouseMarbleX = p2HouseX + HOUSE_WIDTH / 2;
        int currentHouseMarbleY = p2HouseY + 2 * MARBLE_DIAMETER;

        currentHouseMarbleX = currentHouseMarbleX - 2 * (MARBLE_DIAMETER + marbleSpacingX);
        currentHouseMarbleY = currentHouseMarbleY - MARBLE_DIAMETER - marbleSpacingY;

        drawHousesMarbles(graphics, currentHouseMarbleX, currentHouseMarbleY, player2Score);

        // draw wells and marbles in wells
        final int p1WellsY = boardY + BOARD_HEIGHT - WELL_DIAMETER - PADDING;
        final int p2WellsY = boardY + PADDING;
        int currentX = p2HouseX + HOUSE_WIDTH + PADDING;

        for (int i = 0; i < board.getNumWells(); ++i) {

            int currentMarbleX = currentX + WELL_DIAMETER / 2 - MARBLE_DIAMETER - marbleSpacingX;
            int currentMarbleY = p1WellsY;

            graphics.setColor(Color.decode(WELL_COLOR));
            Ellipse2D oval = new Ellipse2D.Double(currentX, p1WellsY, WELL_DIAMETER, WELL_DIAMETER);
            graphics.fill(oval);

            if (setupWells1[i]) {
                wells1.set(i, oval);
                setupWells1[i] = false;
            }

            int numMarbles1 = board.getWellMarbles(Player.PlayerOne, i);
            graphics.setColor(Color.decode(TEXT_COLOR));
            drawCenteredString(
                graphics,
                String.valueOf(numMarbles1),
                new Rectangle(
                    currentX,
                    p1WellsY - PADDING - LABEL_FONT.getSize() / 2,
                    WELL_DIAMETER,
                    LABEL_FONT.getSize()),
                LABEL_FONT);

            //Drawing marbles for wells player1
            currentMarbleX = currentMarbleX - 2 * marbleSpacingX;
            currentMarbleY = currentMarbleY + MARBLE_DIAMETER + marbleSpacingY;
            drawWellsMarbles(graphics, currentMarbleX, currentMarbleY, numMarbles1);

            graphics.setColor(Color.decode(WELL_COLOR));
            oval = new Ellipse2D.Double(currentX, p2WellsY, WELL_DIAMETER, WELL_DIAMETER);
            graphics.fill(oval);
            if (setupWells2[i]) {
                wells2.set(board.flipWellNumber(i), oval);
                setupWells2[i] = false;
            }

            int numMarbles2 = board.getWellMarbles(Player.PlayerTwo, board.flipWellNumber(i));

            graphics.setColor(Color.decode(TEXT_COLOR));
            drawCenteredString(
                graphics,
                String.valueOf(numMarbles2),
                new Rectangle(
                    currentX,
                    p2WellsY + WELL_DIAMETER + PADDING,
                    WELL_DIAMETER,
                    LABEL_FONT.getSize()),
                LABEL_FONT);
            currentX += WELL_DIAMETER + PADDING;

            //Drawing marbles for wells player2
            currentMarbleY = p2WellsY + MARBLE_DIAMETER + marbleSpacingY;
            drawWellsMarbles(graphics, currentMarbleX, currentMarbleY, numMarbles2);
        }

        // draw player 1 house
        final int p1HouseY = boardY + BOARD_HEIGHT - HOUSE_HEIGHT - PADDING;
        graphics.setColor(Color.decode(WELL_COLOR));
        graphics.fillRoundRect(currentX, p1HouseY, HOUSE_WIDTH, HOUSE_HEIGHT, HOUSE_ROUNDED_CORNER, HOUSE_ROUNDED_CORNER);

        graphics.setColor(Color.decode(TEXT_COLOR));
        drawCenteredString(
            graphics,
            String.valueOf(board.getHouseMarbles(Player.PlayerOne)),
            new Rectangle(
                currentX,
                p1HouseY - PADDING - LABEL_FONT.getSize() / 2,
                HOUSE_WIDTH,
                LABEL_FONT.getSize()
            ),
            LABEL_FONT);

        //draw player 1 house marbles
        int player1Score = board.getHouseMarbles(Player.PlayerOne);
        currentHouseMarbleX = currentX + HOUSE_WIDTH / 2;
        currentHouseMarbleY = p1HouseY + 2 * MARBLE_DIAMETER;

        currentHouseMarbleX = currentHouseMarbleX - 2 * (MARBLE_DIAMETER + marbleSpacingX);
        currentHouseMarbleY = currentHouseMarbleY - MARBLE_DIAMETER - marbleSpacingX;

        drawHousesMarbles(graphics, currentHouseMarbleX, currentHouseMarbleY, player1Score);
    }

    private Color createRandomColor() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return new Color(r, g, b);
    }

    private void drawHousesMarbles(Graphics2D g, int currentHouseMarbleX, int currentHouseMarbleY, int playerScore) {
        int copyCurrentHouseMarbleX = currentHouseMarbleX;
        int marbleCounter = 0;

        for (int y = 0; y < HOUSE_MARBLES_ROWS; y++) {

            currentHouseMarbleX = copyCurrentHouseMarbleX;

            for (int x = 0; x < HOUSE_MARBLES_COLUMNS; x++) {

                if (marbleCounter + 1 > playerScore) {
                    return;
                }

                Ellipse2D oval = new Ellipse2D.Double(currentHouseMarbleX, currentHouseMarbleY, MARBLE_DIAMETER, MARBLE_DIAMETER);
                g.setColor(createRandomColor());
                g.fill(oval);
                marbleCounter++;

                currentHouseMarbleX = currentHouseMarbleX + MARBLE_DIAMETER + marbleSpacingX;
            }
            currentHouseMarbleY = currentHouseMarbleY + MARBLE_DIAMETER + marbleSpacingY;
        }
    }

    private void drawWellsMarbles(Graphics2D g, int currentMarbleX, int currentMarbleY, int numMarbles) {
        int copyMarbleX = currentMarbleX;
        int marbleCounter = 0;

        for (int y = 0; y < WELL_MARBLES_ROWS; y++) {

            currentMarbleX = copyMarbleX;

            for (int x = 0; x < WELL_MARBLES_COLUMNS; x++) {

                if (marbleCounter == numMarbles) {
                    return;
                }

                Ellipse2D oval = new Ellipse2D.Double(currentMarbleX, currentMarbleY, MARBLE_DIAMETER, MARBLE_DIAMETER);
                g.setColor(createRandomColor());
                g.fill(oval);
                marbleCounter++;

                currentMarbleX = currentMarbleX + MARBLE_DIAMETER + marbleSpacingX;
            }
            currentMarbleY = currentMarbleY + MARBLE_DIAMETER + marbleSpacingY;
        }
    }

    @Override
    protected Map<Button, JButton> getButtonMap() {
        return Map.ofEntries(
            entry(Button.LeaveButton, leaveButton),
            entry(Button.SubmitButton, submitMoveFromTextButton),
            entry(Button.PieRuleButton, pieRuleButton),
            entry(Button.VoiceButton, voiceButton)
        );
    }

    public enum Button {
        LeaveButton,
        SubmitButton,
        PieRuleButton,
        VoiceButton
    }

    public void updateVoice(boolean b){
        String path;
        if(b){
            path = "img/resizedMute.png";
            voiceButton.setToolTipText("UnMute Music");
        }
        else{
            path = "img/resizedUnmute.png";
            voiceButton.setToolTipText("Mute Music");
        }

        Image voiceIcon = getImage(path);
        voiceButton.setIcon(new ImageIcon(voiceIcon));
    }
}
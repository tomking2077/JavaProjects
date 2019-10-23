package kalah.networking;

import kalah.gamemanager.Player;
import utils.MathUtils;

public class Info {
    private static final int NUM_HOLES_POSITION = 1;
    private static final int NUM_SEEDS_POSITION = 2;
    private static final int TIME_POSITION = 3;
    private static final int PLAYER_POSITION = 4;
    private static final int MARBLE_CONFIGURATION_POSITION = 5;
    private static final int RANDOM_MARBLE_STARTING_POSITION = 6;
    private Player player;
    private int msTime;
    private boolean randomDistribution;
    private int numMarbles;
    private int numWells;
    private int[] wellDistribution;

    public Info(int numWells, int numMarbles, boolean randomDistribution, int msTime) {
        this.randomDistribution = randomDistribution;
        this.numWells = numWells;
        this.numMarbles = numMarbles;
        if (randomDistribution) {
            wellDistribution = MathUtils.randomSequence(numMarbles * numWells, numWells);
        } else {
            wellDistribution = new int[numWells];
            for (int i = 0; i < wellDistribution.length; ++i) {
                wellDistribution[i] = numMarbles;
            }
        }
        this.msTime = msTime;
    }

    /*
     * The first two numbers are integers giving the number of holes and seeds per hole, respectively.
     * Next is the time the client has to move (note: this might be different from the server’s time).
     * Next is either an F or an S, to denote whether the client will be the First Player (F) or Second Player (S).
     * Next is either an S (for a standard configuration with equal seeds in each hole) or
     *  R (for a random configuration with differing seeds in each hole.  If random, there will be a list of the
     *  number of seeds per hole, with one integer per hole (on each side).
     */
    Info(String info) {
        String[] parameters = info.split("\\s+");
        int numWells = Integer.parseInt(parameters[NUM_HOLES_POSITION]);
        wellDistribution = new int[numWells];
        int numSeedsPerWell = Integer.parseInt(parameters[NUM_SEEDS_POSITION]);
        switch (parameters[PLAYER_POSITION]) {
            case "F":
                player = Player.PlayerOne;
                break;
            case "S":
                player = Player.PlayerTwo;
                break;
            default:
                throw new IllegalArgumentException("Invalid player specification");
        }

        msTime = Integer.parseInt(parameters[TIME_POSITION]);

        switch (parameters[MARBLE_CONFIGURATION_POSITION]) {
            case "S":
                wellDistribution = new int[numWells];
                for (int i = 0; i < wellDistribution.length; ++i) {
                    wellDistribution[i] = numSeedsPerWell;
                }
                break;
            case "R":
                for (int i = RANDOM_MARBLE_STARTING_POSITION; i < parameters.length; ++i) {
                    wellDistribution[i - RANDOM_MARBLE_STARTING_POSITION] = Integer.parseInt(parameters[i]);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid marble specification");
        }
    }

    public Player getPlayer() {
        return player;
    }

    public int getMsTime() {
        return msTime;
    }

    public int[] getWellDistribution() {
        return wellDistribution;
    }

    public String getInfoString(Player clientPlayer) {
        String playerString = "";
        switch (clientPlayer) {
            case PlayerOne:
                playerString = "F";
                break;
            case PlayerTwo:
                playerString = "S";
                break;
        }
        String configurationString;
        if (randomDistribution) {
            configurationString = "R";
        } else {
            configurationString = "S";
        }
        String infoString = String.format("INFO %d %d %d %s %s", numWells, numMarbles, msTime, playerString, configurationString);
        if (randomDistribution) {
            StringBuilder stringBuilder = new StringBuilder(infoString);
            for (int i : wellDistribution) {
                stringBuilder.append(" ").append(i);
            }
            infoString = stringBuilder.toString();
        }
        return infoString;
    }
}

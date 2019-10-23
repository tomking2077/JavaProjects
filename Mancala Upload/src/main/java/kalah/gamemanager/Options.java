package kalah.gamemanager;

public class Options {
    public static final Integer MINIMUM_WELLS = 4;
    public static final Integer MAXIMUM_WELLS = 9;
    public static final Long DEFAULT_LOCAL_AI_MOVE_TIME = 5000L;
    public static final Long DEFAULT_CLIENT_AI_MOVE_TIME = 15000L;
    private Integer numWells;
    private Integer numMarbles;
    private Boolean randomDistribution;
    private Boolean pieRule;

    public Options() {

    }

    public Options(int numWells, int numMarbles, boolean randomDistribution, boolean pieRule) {
        this.numWells = numWells;
        this.numMarbles = numMarbles;
        this.randomDistribution = randomDistribution;
        this.pieRule = pieRule;
    }

    public int getNumWells() {
        return numWells;
    }

    public int getNumMarbles() {
        return numMarbles;
    }

    public boolean isRandomDistribution() {
        return randomDistribution;
    }

    public boolean isPieRule() {
        return pieRule;
    }

    public void setPieRule(boolean pieRule) {
        this.pieRule = pieRule;
    }

    public void setRandomDistribution(boolean randomDistribution) {
        this.randomDistribution = randomDistribution;
    }

    public void setNumWells(Integer numWells) {
        this.numWells = numWells;
    }

    public void setNumMarbles(int numMarbles) {
        this.numMarbles = numMarbles;
    }
}

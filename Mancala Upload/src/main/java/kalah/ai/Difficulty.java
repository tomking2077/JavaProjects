package kalah.ai;

import java.text.ParseException;

public enum Difficulty {
    VeryEasy(1),
    Easy(0),
    Medium(1),
    Hard(2),
    Expert(5),
    Insane(6),
    Infinite(Integer.MAX_VALUE);

    private final int depth;

    Difficulty(int depth) {
        this.depth = depth;
    }

    public static Difficulty parseDifficulty(String difficulty) throws ParseException {
        switch (difficulty.toLowerCase()) {
            case "veryeasy":
            case "very easy":
                return VeryEasy;
            case "easy":
                return Easy;
            case "medium":
                return Medium;
            case "hard":
                return Hard;
            case "expert":
                return Expert;
            case "insane":
                return Insane;
            case "infinite":
                return Infinite;
        }

        throw new ParseException("kalah.ai.Difficulty.parseDifficulty() failed: unable to parse", 0);
    }

    public int getDepth() {
        return this.depth;
    }

    @Override
    public String toString() {
        switch (this) {
            case VeryEasy:
                return "Very Easy";
            case Easy:
                return "Easy";
            case Medium:
                return "Medium";
            case Hard:
                return "Hard";
            case Expert:
                return "Expert";
            case Insane:
                return "Insane";
            case Infinite:
                return "Infinite";
            default:
                return null;
        }
    }
}

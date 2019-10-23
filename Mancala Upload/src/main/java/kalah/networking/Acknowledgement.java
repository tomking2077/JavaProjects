package kalah.networking;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum Acknowledgement {
    WELCOME,
    READY,
    OK,
    ILLEGAL,
    TIME,
    LOSER,
    WINNER,
    TIE;

    public static Set<Acknowledgement> gameEndingSet =
        new HashSet<>(Arrays.asList(ILLEGAL, TIME, LOSER, WINNER, TIE));

    public static boolean isGameEndingAcknowledgement(String acknowledgementString) {
        Acknowledgement acknowledgement;
        try {
            acknowledgement = Acknowledgement.valueOf(acknowledgementString);
        } catch (Exception e) {
            return false;
        }
        return gameEndingSet.contains(acknowledgement);
    }
}

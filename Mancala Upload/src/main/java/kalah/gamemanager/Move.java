package kalah.gamemanager;

import java.util.Optional;

public class Move {
    private UserMoveType moveType;
    private Integer wellNumber;

    public Move(UserMoveType moveType, Integer wellNumber) {
        this.moveType = moveType;
        this.wellNumber = wellNumber;
    }

    public Move(UserMoveType moveType) {
        this.moveType = moveType;
        this.wellNumber = null;
    }

    public UserMoveType getUserMoveType() {
        return moveType;
    }

    public Optional<Integer> getWellNumber() {
        if (wellNumber == null) {
            return Optional.empty();
        }

        return Optional.of(wellNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Move move = (Move) o;

        if (moveType != move.moveType) return false;
        return wellNumber != null ? wellNumber.equals(move.wellNumber) : move.wellNumber == null;
    }

    @Override
    public int hashCode() {
        int result = moveType.hashCode();
        result = 31 * result + (wellNumber != null ? wellNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if (getUserMoveType() == UserMoveType.PieMove) {
            return "P";
        } else if (getUserMoveType() == UserMoveType.WellMove && getWellNumber().isPresent()) {
            return "W" + getWellNumber().get().toString();
        }

        return "";
    }
}

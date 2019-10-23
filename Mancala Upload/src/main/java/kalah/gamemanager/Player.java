package kalah.gamemanager;

public enum Player {
    PlayerOne,
    PlayerTwo;

    public Player flipPlayer() {
        if (this == PlayerOne) {
            return PlayerTwo;
        } else {
            return PlayerOne;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case PlayerOne:
                return "Player One";
            case PlayerTwo:
                return "Player Two";
            default:
                return null;
        }
    }
}

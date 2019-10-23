package kalah.gamemanager;

import utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class Board implements Cloneable {
    private int p1House;
    private int[] p1Row;
    private int p2House;
    private int[] p2Row;

    public Board(Options options) {
        int numWells = options.getNumWells();
        int numMarbles = options.getNumMarbles();
        p1Row = new int[numWells];
        p2Row = new int[numWells];
        if (options.isRandomDistribution()) {
            int[] randomSequence = MathUtils.randomSequence(numMarbles * numWells, numWells);
            for (int i = 0; i < numWells; ++i) {
                int marbleCount = randomSequence[i];
                p1Row[i] = marbleCount;
                p2Row[i] = marbleCount;
            }
        } else {
            for (int i = 0; i < numWells; ++i) {
                p1Row[i] = numMarbles;
                p2Row[i] = numMarbles;
            }
        }
    }

    public Board(int p1House, int[] p1Row, int p2House, int[] p2Row) {
        this.p1House = p1House;
        this.p1Row = p1Row;
        this.p2House = p2House;
        this.p2Row = p2Row;
    }

    public Board clone() {
        return new Board(p1House, p1Row.clone(), p2House, p2Row.clone());
    }

    void printBoardState() {
        for (int i = 0; i < p1Row.length / 2; ++i) {
            System.out.print("\t");
        }
        System.out.println("Player 2");
        System.out.print("\t");
        for (int i = p2Row.length - 1; i >= 0; --i) {
            System.out.print(p2Row[i] + "\t");
        }
        System.out.println();
        System.out.print(p2House);
        for (int i = 0; i <= p1Row.length; ++i) {
            System.out.print("\t");
        }
        System.out.print(p1House);
        System.out.println();
        System.out.print("\t");
        for (int i : p1Row) {
            System.out.print(i + "\t");
        }
        System.out.println();
        for (int i = 0; i < p1Row.length / 2; ++i) {
            System.out.print("\t");
        }
        System.out.println("Player 1");
    }

    public int getNumWells() {
        return p1Row.length;
    }

    public List<Move> getValidMoves(Player player) {
        List<Move> result = new ArrayList<>();

        for (int i = 0; i < getNumWells(); ++i) {
            if (getWellMarbles(player, i) > 0) {
                result.add(new Move(UserMoveType.WellMove, i));
            }
        }

        return result;
    }

    public int getWellMarbles(Player player, int wellNumber) {
        if (wellNumber >= getNumWells()) {
            throw new IllegalArgumentException("wellNumber out of range");
        }
        switch (player) {
            case PlayerOne:
                return p1Row[wellNumber];
            case PlayerTwo:
                return p2Row[wellNumber];
            default:
                throw new IllegalArgumentException();
        }
    }

    public int getWellMarbles(Player player) {
        int[] row;
        int result = 0;

        switch (player) {
            case PlayerOne:
                row = p1Row;
                break;
            case PlayerTwo:
                row = p2Row;
                break;
            default:
                throw new IllegalArgumentException();
        }

        for (int i : row) {
            result += i;
        }

        return result;
    }

    public int getHouseMarbles(Player player) {
        switch (player) {
            case PlayerOne:
                return p1House;
            case PlayerTwo:
                return p2House;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void swapHouses() {
        int temp = p1House;
        p1House = p2House;
        p2House = temp;
    }

    private void swapWells() {
        int[] temp = p1Row.clone();
        p1Row = p2Row.clone();
        p2Row = temp;
    }

    public void pieMove() {
        swapHouses();
        swapWells();
    }

    private void emptyWell(Player player, int wellNumber) {
        switch (player) {
            case PlayerOne:
                p1Row[wellNumber] = 0;
                break;
            case PlayerTwo:
                p2Row[wellNumber] = 0;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void emptyWells(Player player) {
        for (int i = 0; i < getNumWells(); ++i) {
            emptyWell(player, i);
        }
    }

    private void incrementHouseMarbles(Player player) {
        incrementHouseMarbles(player, 1);
    }

    private void incrementHouseMarbles(Player player, int marbleCount) {
        switch (player) {
            case PlayerOne:
                p1House += marbleCount;
                break;
            case PlayerTwo:
                p2House += marbleCount;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void incrementWellMarbles(Player player, int wellNumber) {
        switch (player) {
            case PlayerOne:
                ++p1Row[wellNumber];
                break;
            case PlayerTwo:
                ++p2Row[wellNumber];
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void captureMarbles(Player player, int wellNumber) {
        //Capture opposite marbles
        incrementHouseMarbles(player, getWellMarbles(player.flipPlayer(), flipWellNumber(wellNumber)));
        emptyWell(player.flipPlayer(), flipWellNumber(wellNumber));
        //Capture same-side marble
        incrementHouseMarbles(player, 1);
        emptyWell(player, wellNumber);
    }

    public int flipWellNumber(int wellNumber) {
        return getNumWells() - wellNumber - 1;
    }

    public MoveType moveMarbles(Player player, int wellNumber) {
        if (wellNumber >= p1Row.length || wellNumber < 0) {
            return MoveType.IllegalMove;
        }

        int marbleCount = getWellMarbles(player, wellNumber);
        emptyWell(player, wellNumber);
        if (marbleCount == 0) {
            return MoveType.IllegalMove;
        }

        MoveType moveType = MoveType.StandardMove;
        ++wellNumber;
        Player playerSide = player;
        while (marbleCount > 0) {
            if (wellNumber == getNumWells()) {
                if (player == playerSide) {
                    incrementHouseMarbles(player);
                    --marbleCount;
                    if (marbleCount == 0) {
                        moveType = MoveType.HouseMove;
                    }
                }
                wellNumber = 0;
                playerSide = playerSide.flipPlayer();
            } else {
                incrementWellMarbles(playerSide, wellNumber);
                --marbleCount;
                if (marbleCount == 0) {
                    //Check for a capturing move
                    if (player == playerSide) {
                        if (getWellMarbles(player, wellNumber) == 1 && getWellMarbles(player.flipPlayer(), flipWellNumber(wellNumber)) > 0) {
                            captureMarbles(player, wellNumber);
                            moveType = MoveType.CaptureMove;
                        }
                    }
                }
                ++wellNumber;
            }
        }
        if (isGameOver()) {
            p1House += getWellMarbles(Player.PlayerOne);
            p2House += getWellMarbles(Player.PlayerTwo);
            emptyWells(Player.PlayerOne);
            emptyWells(Player.PlayerTwo);
        }
        return moveType;
    }

    public boolean isGameOver() {
        int p1Sum = 0;
        for (int i : p1Row) {
            p1Sum += i;
        }
        int p2Sum = 0;
        for (int i : p2Row) {
            p2Sum += i;
        }
        return (p1Sum == 0 || p2Sum == 0);
    }

    public Player getWinner() {
        //Doesn't verify if the game is over
        if (p1House > p2House) {
            return Player.PlayerOne;
        } else if (p1House < p2House) {
            return Player.PlayerTwo;
        } else {
            //Game is a tie
            return null;
        }
    }
}

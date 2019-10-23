import kalah.gamemanager.GameManager;
import kalah.gamemanager.GameState;
import kalah.gamemanager.Move;
import kalah.gamemanager.MoveType;
import kalah.gamemanager.Player;
import kalah.gamemanager.UserMoveType;
import kalah.networking.Acknowledgement;
import kalah.networking.Client;
import kalah.networking.ConnectionInformation;
import kalah.networking.Info;
import kalah.networking.InvalidCommandException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleMain {
    private Client client;
    private GameManager gameManager;
    private int msTime;
    private Player player;

    public ConsoleMain(String serverAddress, int portNumber) throws IOException, InvalidCommandException {
        client = new Client(new ConnectionInformation(serverAddress, portNumber));

        client.readWelcome();

        //Parse Info Message
        Info info = client.readInfo();
        msTime = info.getMsTime();
        player = info.getPlayer();
        gameManager = new GameManager(info);
        client.sendReady();

        launchGame();
    }

    public static void main(String[] args) throws IOException, InvalidCommandException {
        new ConsoleMain(args[0], Integer.parseInt(args[1]));
    }

    private static Move getConsoleMove() {
        try {
            System.out.println(">");
            String input = new Scanner(System.in).nextLine();
            if (input.equals("P")) {
                return new Move(UserMoveType.PieMove);
            } else {
                int wellNumber = Integer.valueOf(input);
                return new Move(UserMoveType.WellMove, --wellNumber);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal Move!");
        }
    }

    public void launchGame() throws IOException, InvalidCommandException {
        while (true) {
            if (player == gameManager.getActivePlayer() && gameManager.getGameState() != GameState.GameOver) {
                List<Move> moveList = getConsoleMoves();
                client.sendMoves(moveList);
                String response = client.receiveMessage();
                if (Acknowledgement.isGameEndingAcknowledgement(response)) {
                    abortGame(response);
                }
            } else {
                String response = client.receiveMessage();
                if (Acknowledgement.isGameEndingAcknowledgement(response)) {
                    abortGame(response);
                } else {
                    List<Move> moveList = Client.parseMoveString(response);
                    for (Move move : moveList) {
                        gameManager.makeMove(move);
                    }
                    gameManager.printBoardState();
                    client.sendOk();
                }
            }
        }
    }

    private List<Move> getConsoleMoves() {
        gameManager.printBoardState();
        Move move;
        MoveType moveType;
        List<Move> moveList = new ArrayList<>();
        do {
            move = getConsoleMove();
            moveList.add(move);
            moveType = gameManager.makeMove(move);
            gameManager.printBoardState();
        } while (moveType == MoveType.HouseMove);
        return moveList;
    }

    private void abortGame(String response) throws IOException {
        String result;
        if (response.equals(Acknowledgement.ILLEGAL.name())) {
            System.out.println("ILLEGAL MOVE MADE!");
            result = client.receiveMessage();
        } else if (response.equals(Acknowledgement.TIME.name())) {
            System.out.println("TIME OUT!");
            result = client.receiveMessage();
        } else {
            result = response;
        }
        printResult(result);
        client.close();
        System.exit(0);
    }

    private void printResult(String result) {
        switch (Acknowledgement.valueOf(result)) {
            case LOSER:
                System.out.println("YOU LOSE!");
                break;
            case TIE:
                System.out.println("YOU DIDN'T LOSE, BUT YOU'RE NOT A WINNER");
                break;
            case WINNER:
                System.out.println("WOW! YOU ACTUALLY WON SOMETHING");
                break;
        }
    }
}
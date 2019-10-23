package kalah.networking;

import kalah.gamemanager.GameManager;
import kalah.gamemanager.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * command ::== info| move | ack
 * info ::== INFO game_config
 * move ::== hole (hole)* | P
 * ack ::== WELCOME | READY |OK | ILLEGAL | TIME | LOSER | WINNER | TIE
 * game_config ::== <int for holes per side> <int for seeds per side> <long int for time> <F | S> <S | R hole_config>
 * hole_config ::== <int for seeds in hole 1> … <int for seeds in hole h
 */
public abstract class Server {
    protected ServerSocket serverSocket;
    protected GameManager gameManager;
    protected Info info;
    private BufferedReader input1;
    private PrintWriter output1;
    private BufferedReader input2;
    private PrintWriter output2;

    public Server(int portNumber, Info info) throws InvalidCommandException {
        this.info = info;
        try {
            serverSocket = new ServerSocket(portNumber);
            gameManager = new GameManager(info);

            init();

            Socket clientSocket1 = serverSocket.accept();
            input1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));
            output1 = new PrintWriter(clientSocket1.getOutputStream(), true);
            sendWelcome(Player.PlayerOne);

            Socket clientSocket2 = serverSocket.accept();
            input2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));
            output2 = new PrintWriter(clientSocket2.getOutputStream(), true);
            sendWelcome(Player.PlayerTwo);

            sendInfo();

            readReady(Player.PlayerOne);
            readReady(Player.PlayerTwo);

            launchGame();
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + portNumber);
            System.exit(-1);
        }
    }

    protected abstract void init();

    protected abstract void launchGame() throws IOException, InvalidCommandException;

    protected void sendMessage(Player player, String message) {
        switch (player) {
            case PlayerOne:
                output1.println(message);
                System.out.println("Debug: Message Sent to Client 1 - " + message);
                break;
            case PlayerTwo:
                output2.println(message);
                System.out.println("Debug: Message Sent to Client 2 - " + message);
                break;
        }
    }

    protected String receiveMessage(Player player) throws IOException {
        String message = "";
        switch (player) {
            case PlayerOne:
                message = input1.readLine();
                System.out.println("Debug: Message Received from Client 1 - " + message);
                break;
            case PlayerTwo:
                message = input2.readLine();
                System.out.println("Debug: Message Received from Client 2 - " + message);
                break;
        }
        return message;
    }

    private void sendWelcome(Player player) {
        sendMessage(player, Acknowledgement.WELCOME.toString());
    }

    private void sendInfo() {
        sendMessage(Player.PlayerOne, info.getInfoString(Player.PlayerOne));
        sendMessage(Player.PlayerTwo, info.getInfoString(Player.PlayerTwo));
    }

    private void readReady(Player player) throws IOException, InvalidCommandException {
        String response = receiveMessage(player);
        if (!response.equals(Acknowledgement.READY.toString())) {
            throw new InvalidCommandException("Expected READY Acknowledgement");
        }
    }

    protected void readOk(Player player) throws IOException, InvalidCommandException {
        String response = receiveMessage(player);
        if (!response.equals(Acknowledgement.OK.toString())) {
            throw new InvalidCommandException("Expected OK Acknowledgement");
        }
    }

    protected void sendOk(Player player) {
        sendMessage(player, Acknowledgement.OK.toString());
    }

    private void sendIllegal(Player player) {
        sendMessage(player, Acknowledgement.ILLEGAL.toString());
    }

    private void sendTimeOut(Player player) {
        sendMessage(player, Acknowledgement.TIME.toString());
    }

    protected void illegalAbortion(Player player) {
        try {
            sendIllegal(Player.PlayerOne);
            sendIllegal(Player.PlayerTwo);
            sendMessage(player, Acknowledgement.LOSER.toString());
            sendMessage(player.flipPlayer(), Acknowledgement.WINNER.toString());

            waitForClientsToQuit();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.exit(0);
    }

    protected void timeOutAbortion(Player player) {
        try {
            //Notify winner
            sendTimeOut(player.flipPlayer());
            sendMessage(player.flipPlayer(), Acknowledgement.WINNER.toString());

            //Notify loser
            sendTimeOut(player);
            sendMessage(player, Acknowledgement.LOSER.toString());

            waitForClientsToQuit();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.exit(0);
    }

    protected void standardAbortion() {
        try {
            Player winner = gameManager.getWinner();
            if (winner != null) {
                sendMessage(winner, Acknowledgement.WINNER.toString());
                sendMessage(winner.flipPlayer(), Acknowledgement.LOSER.toString());
            } else {
                sendMessage(Player.PlayerOne, Acknowledgement.TIE.toString());
                sendMessage(Player.PlayerTwo, Acknowledgement.TIE.toString());
            }

            waitForClientsToQuit();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.exit(0);
    }

    private void waitForClientsToQuit() throws IOException {
        waitForSocketClose(Player.PlayerOne);
        waitForSocketClose(Player.PlayerTwo);
    }

    private void waitForSocketClose(Player player) throws IOException {
        switch (player) {
            case PlayerOne:
                while (true) {
                    if (input1.read() == -1) break;
                }
                break;
            case PlayerTwo:
                while (true) {
                    if (input2.read() == -1) break;
                }
                break;
        }
    }

    public class TimedMessageReaderFromActivePlayer implements Callable<String> {
        @Override
        public String call() throws IOException {
            return receiveMessage(gameManager.getActivePlayer());
        }
    }
}

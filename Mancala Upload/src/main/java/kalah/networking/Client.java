package kalah.networking;

import kalah.gamemanager.Move;
import kalah.gamemanager.UserMoveType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public Client(ConnectionInformation connect) throws IOException {
        socket = new Socket(connect.getServerAddress(), connect.getSocketNumber());
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
    }

    public static List<Move> parseMoveString(String moveString) {
        List<Move> moveList = new ArrayList<>();
        String[] moveStringArray = moveString.split("\\s+");
        for (String s : moveStringArray) {
            if (s.equals("P")) {
                moveList.add(new Move(UserMoveType.PieMove));
            } else {
                int wellNumber = Integer.parseInt(s);
                moveList.add(new Move(UserMoveType.WellMove, --wellNumber));
            }
        }
        return moveList;
    }

    private void sendMessage(String message) {
        output.println(message);
        System.out.println("Debug: Message Sent - " + message);
    }

    public String receiveMessage() throws IOException {
        String message = input.readLine();
        System.out.println("Debug: Message Received - " + message);
        return message;
    }

    public void readWelcome() throws IOException, InvalidCommandException {
        String response = receiveMessage();
        if (!response.equals(Acknowledgement.WELCOME.toString())) {
            throw new InvalidCommandException("Expected WELCOME Acknowledgement");
        }
    }

    public Info readInfo() throws IOException, InvalidCommandException {
        String response = receiveMessage();
        try {
            return new Info(response);
        } catch (Exception e) {
            throw new InvalidCommandException("Invalid INFO Command");
        }
    }

    public void sendReady() throws IOException {
        sendMessage(Acknowledgement.READY.toString());
    }

    public void sendMoves(List<Move> moveList) {
        StringBuilder message = new StringBuilder();
        String separator = "";
        for (Move move : moveList) {
            if (move.getUserMoveType() == UserMoveType.PieMove) {
                message.append("P");
            } else if (move.getUserMoveType() == UserMoveType.WellMove) {
                int wellNumber = move.getWellNumber().get();
                message.append(separator).append(++wellNumber);
                separator = " ";
            }
        }
        sendMessage(message.toString());
    }

    public List<Move> readMoves() throws IOException {
        String message = input.readLine();
        return parseMoveString(message);
    }

    public void sendOk() {
        sendMessage(Acknowledgement.OK.toString());
    }

    public void readOk() throws IOException, InvalidCommandException {
        String message = receiveMessage();
        if (!message.equals(Acknowledgement.OK.toString())) {
            throw new InvalidCommandException("Expected OK Acknowledgement");
        }
    }

    public void close() throws IOException {
        socket.close();
    }
}

//public class Client {
//    private Socket socket;
//    private BufferedReader input;
//    private PrintWriter output;
//
//    public Client(String serverAddress, int portNumber) throws IOException {
//        socket = new Socket(serverAddress, portNumber);
//        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        output = new PrintWriter(socket.getOutputStream(),true);
//        readWelcome();
//        readInfo();
//        sendReady();
//    }
//
//    private void readWelcome() throws IOException {
//        String response = input.readLine();
//        if(!response.equals(Acknowledgement.WELCOME.toString())){
//            throw new IOException();
//        }
//    }
//
//    private void readInfo() throws IOException {
//        String response = input.readLine();
//        System.out.println("INFO Configuration: " + response);
//    }
//
//    private void sendReady() throws IOException {
//        output.println(Acknowledgement.READY);
//    }
//}

package kalah.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class BareBonesClient {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public BareBonesClient(String serverAddress, int portNumber) throws IOException {
        socket = new Socket(serverAddress, portNumber);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
    }

    public static void main(String[] args) throws IOException {
        new BareBonesClient(args[0], Integer.parseInt(args[1])).communicate();
    }

    public void communicate() {
        SocketListener socketListener = new SocketListener();
        Thread socketListenerThread = new Thread(socketListener);
        socketListenerThread.start();

        while (true) {
            String input = new Scanner(System.in).nextLine();
            output.println(input);
        }
    }

    public class SocketListener implements Runnable {
        public void run() {
            String line;
            try {
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
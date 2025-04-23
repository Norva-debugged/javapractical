package javapractical;
import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on localhost:" + PORT);
            System.out.println("Waiting for client...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected from: " + clientSocket.getInetAddress().getHostAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            Thread receiveThread = new Thread(() -> {
                try {
                    String clientMessage;
                    while ((clientMessage = in.readLine()) != null) {
                        System.out.println("\nClient: " + clientMessage);
                        System.out.print("You: ");
                        if (clientMessage.equalsIgnoreCase("bye")) {
                            System.out.println("\nClient left the chat");
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("\nClient disconnected");
                }
            });
            receiveThread.start();

            System.out.println("Start chatting (type 'bye' to exit):");
            String message;
            while (true) {
                System.out.print("You: ");
                message = consoleReader.readLine();
                out.println(message);
                if (message.equalsIgnoreCase("bye")) {
                    break;
                }
            }

            in.close();
            out.close();
            consoleReader.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
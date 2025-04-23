package javapractical.DIFFdEVICE;
import java.io.*;
import java.net.*;

public class Client {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter server IP: ");
            String serverIP = consoleReader.readLine();
            Socket socket = new Socket(serverIP, PORT);
            System.out.println("Connected to server at " + serverIP + ":" + PORT);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            Thread receiveThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println("\nServer: " + serverMessage);
                        System.out.print("You: ");
                        if (serverMessage.equalsIgnoreCase("bye")) {
                            System.out.println("\nServer ended the chat");
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("\nDisconnected from server");
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
            socket.close();
        } catch (UnknownHostException e) {
            System.out.println("Cannot find the server at the specified IP");
        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}
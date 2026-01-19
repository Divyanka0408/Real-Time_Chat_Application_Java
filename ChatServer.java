package chatapp;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        System.out.println(" Chat server started on port: " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket);
                clientHandlers.add(handler);
                handler.start();
            }
        } catch (IOException e) {
            System.out.println(" Server Error: " + e.getMessage());
        }
    }

    static void broadcast(String message, ClientHandler exclude) {
        synchronized (clientHandlers) {
            for (ClientHandler client : clientHandlers) {
                if (client != exclude) {
                    client.sendMessage(message);
                }
            }
        }
    }

    static void remove(ClientHandler client) {
        clientHandlers.remove(client);
    }
}

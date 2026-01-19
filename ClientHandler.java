package chatapp;
import java.io.*;
import java.net.*;

class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String userName;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Enter your username: ");
            userName = in.readLine();
            System.out.println("👤 " + userName + " joined.");
            ChatServer.broadcast("👤 " + userName + " joined the chat.", this);

            String msg;
            while ((msg = in.readLine()) != null) {
                System.out.println(" " + userName + ": " + msg);
                ChatServer.broadcast(userName + ": " + msg, this);
            }
        } catch (IOException e) {
            System.out.println(" Error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {}
            ChatServer.remove(this);
            ChatServer.broadcast(" " + userName + " left the chat.", this);
        }
    }

    void sendMessage(String message) {
        out.println(message);
    }
}

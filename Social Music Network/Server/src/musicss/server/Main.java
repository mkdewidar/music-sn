package musicss.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Program starts here.
 */
public class Main {
    public static void main(String[] args) {
        ServerSocket serverSocket;
        boolean isRunning = true;

        try {
            serverSocket = new ServerSocket(9090);
        } catch (IOException e) {
            System.out.println("ERROR " + e.getMessage());

            return;
        }

        while (isRunning) {
            try {
                System.out.println("Awaiting client connection...");
                Socket userSocket = serverSocket.accept();
                System.out.println("Connected to user: " + userSocket.getInetAddress().getHostAddress());

            } catch (IOException e) {
                System.out.println("ERROR " + e.getMessage());
            }

        }

    }
}

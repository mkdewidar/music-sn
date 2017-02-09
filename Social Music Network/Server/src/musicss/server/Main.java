package musicss.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Program starts here.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Starting server...");

        ServerSocket serverSocket;
        boolean isRunning = true;

        try {
            serverSocket = new ServerSocket(9999);
        } catch (IOException e) {
            System.out.println("ERROR " + e.getMessage());

            return;
        }

        Socket userSocket;
        InputStream socketContentStream;
        while (isRunning) {
            try {
                System.out.println("Awaiting client connection...");
                userSocket = serverSocket.accept();
                socketContentStream = userSocket.getInputStream();

                System.out.println("Connected to user: " + userSocket.getInetAddress().getHostAddress());
            } catch (IOException e) {
                System.out.println("ERROR " + e.getMessage());
                continue;
            }

            // stay with client as long as they are connected
            while (true) {

                try {
                    System.out.println(((char) socketContentStream.read()));
                    System.out.println("Msg from " + userSocket.getInetAddress().getHostAddress());
                } catch (IOException e) {
                    System.out.println("ERROR reading user msg " + e.getMessage() +
                            "\n potentially connection lost");
                    break;
                }
            }

            try {
                socketContentStream.close();
            } catch (IOException e) {
                System.out.println("ERROR " + e.getMessage());
            }
        }

    }
}

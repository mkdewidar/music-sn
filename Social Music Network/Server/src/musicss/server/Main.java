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
            System.err.println("ERROR: Can't create server socket\n\t" + e.getMessage());
            return;
        }

        while (isRunning) {
            System.out.println("Awaiting another client connection...");

            try {
                new Thread(new ServerWorker(serverSocket.accept())).start();
            } catch (IOException e) {
                System.err.println("ERROR: Failed to connect to client or client streams couldn't be created" +
                        "\n\t" + e.getMessage());
            }
        }
    }
}

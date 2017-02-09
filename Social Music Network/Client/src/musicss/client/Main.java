package musicss.client;

import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;

/**
 * Program starts here, any network exceptions that
 * result in the program not working are handled here.
 */
public class Main {
    public static void main(String[] args) {
        Socket socket;
        boolean isRunning;
        BufferedReader inputReader;
        OutputStream intoSocketStream;

        try {
            socket = new Socket(Inet4Address.getByName("localhost"), 9999);
            isRunning = true;
            inputReader = new BufferedReader(new InputStreamReader(System.in));
            intoSocketStream = socket.getOutputStream();

            System.out.println("You have been connected to the server.");
        } catch (IOException e) {
            System.out.println("ERROR " + e.getMessage());

            return;
        }


        while (isRunning) {
            try {
                intoSocketStream.write(inputReader.readLine().getBytes());
            } catch (IOException e) {
                System.out.println("ERROR " + e.getMessage());
                break;
            }
        }

        System.out.println("Disconnecting from server...");
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("ERROR closing socket: " + e.getMessage());
        }
    }
}

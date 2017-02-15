package musicss.client;

import java.io.*;
import java.net.UnknownHostException;

/**
 * Program starts here.
 */
public class Main {
    public static void main(String[] args) {
        NetworkManager networkManager;
        BufferedReader inputReader;

        boolean isRunning = true;

        try {
            networkManager = new NetworkManager();
            inputReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("You have been connected to the server.");

        } catch (UnknownHostException e) {
            System.err.println("ERROR: Couldn't find host address\n\t" + e.getMessage());
            return;
        } catch (IOException e) {
            System.err.println("ERROR: An IO error occurred during setup\n\t" + e.getMessage());
            return;
        }


        while (isRunning) {
            try {
                networkManager.SendString(inputReader.readLine());
            } catch (IOException e) {
                System.err.println("ERROR: Could not send message due to IO error\n\t " + e.getMessage());
                break;
            }
        }

        System.out.println("Disconnecting from server...");
        try {
            networkManager.Close();
        } catch (IOException e) {
            System.err.println("ERROR: An IO error occurred while closing resources\n\t" + e.getMessage());
        }
    }
}

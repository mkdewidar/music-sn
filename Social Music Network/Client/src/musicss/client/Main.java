package musicss.client;

import java.io.IOException;

/**
 * Program starts here, any network exceptions that
 * result in the program not working are handled here.
 */
public class Main {
    public static void main(String[] args) {
        ClientInstance instance;
        try {
            instance = new ClientInstance();
        } catch (IOException e) {
            System.out.println("ERROR " + e.getMessage());

            return;
        }

        try {
            instance.Start();
        } catch (IOException e) {
            System.out.println("ERROR " + e.getMessage());
        }
    }
}

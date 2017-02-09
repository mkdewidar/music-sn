package musicss.client;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;

/**
 * Represents a single client program it is where
 * all of the main code for the client runs.
 *
 * An instance is created by the main function and
 * any connection problems are thrown back to main.
 */
public class ClientInstance {
    private Socket socket;

    public ClientInstance() throws IOException {
        socket = new Socket(Inet4Address.getByName("localhost"), 9090);
    }

    /**
     * Starts the client program
     */
    public void Start() throws IOException {
        System.out.println("You have been connected to the server.");

        System.out.println("Nothing to do, disconnecting.");

        socket.close();
    }
}

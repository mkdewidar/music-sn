package musicss.client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Singleton controller that manages all exterior connections to the application server
 */
public class NetworkController {
    public static NetworkController connectionController = new NetworkController();

    private Socket socket;
    private PrintStream outputStream;

    private NetworkController() {
        connectionController = this;

        socket = new Socket();
    }

    /**
     * Closes all the resources used up by controller.
     */
    public void close() {
        try {
            socket.close();
            // it may be null if the client was never connected to the server
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            System.err.println("ERROR: An IO error occurred while closing resources\n\t" + e.getMessage());
        }
    }

    /**
     * Connects the controller to the server.
     */
    public void connect() throws IOException {
        try {
            socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 9999));
            outputStream = new PrintStream(socket.getOutputStream());

            System.out.println("Successfully connected to the server.");
        } catch (IOException e) {
            System.err.println("ERROR: Couldn't connect to server or get stream\n\t" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Returns whether or not the controller is connected to the server
     */
    public boolean isConnected() {
        // TODO: insert code to check the connection status
        return true;
    }

    /**
     * Sends a given string over the network.
     *
     * @param msg The msg to be sent over the network.
     */
    public void sendString(String msg) {
        if (outputStream != null) {
            outputStream.println(msg);
        }
    }

    /**
     * Reads the string from the socket.

    public String GetString() {
    }
     */
}

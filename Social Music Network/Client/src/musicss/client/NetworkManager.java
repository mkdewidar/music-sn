package musicss.client;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Manages all exterior connections to the application server
 */
public class NetworkManager {
    public static NetworkManager connectionManager = new NetworkManager();

    private Socket socket;
    private PrintStream intoSocketStream;

    /**
     * A private constructor to setup the manager instance while
     *      preventing anyone else from constructing their own.
     */
    private NetworkManager() {
        socket = new Socket();
    }

    /**
     * Closes all the resources used up by network manager.
     */
    public void Close() {
        try {
            socket.close();
            // it may be null if the client was never connected to the server
            if (intoSocketStream != null) {
                intoSocketStream.close();
            }
        } catch (IOException e) {
            System.err.println("ERROR: An IO error occurred while closing resources\n\t" + e.getMessage());
        }
    }

    /**
     * Connects the manager to the server.
     *
     * @throws IOException When the client can't connect to the server and/or the
     *                      stream can't be obtained.
     */
    public void Connect() throws IOException
    {
        socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 9999));
        intoSocketStream = new PrintStream(socket.getOutputStream());
    }

    /**
     * Sends a given string over the network.
     *
     * @param msg The msg to be sent over the network.
     *
     * @throws IOException When the connection hasn't been properly created.
     */
    public void SendString(String msg) throws IOException {
        if (intoSocketStream != null) {
            intoSocketStream.println(msg);
        } else {
            throw new IOException();
        }
    }

    /**
     * Reads the string from the socket.

    public String GetString() {
    }
     */
}

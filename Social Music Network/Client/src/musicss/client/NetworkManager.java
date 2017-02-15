package musicss.client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Manages all exterior connections to the application server
 */
public class NetworkManager {
    private Socket socket;
    private PrintStream intoSocketStream;

    /**
     * Creates the network manager instance.
     *
     * @throws UnknownHostException When the address of the server couldn't be resolved.
     *
     * @throws IOException When an IO error occurs while creating the socket or accessing a stream.
     */
    public NetworkManager() throws UnknownHostException, IOException{
        socket = new Socket(InetAddress.getByName("localhost"), 9999);
        intoSocketStream = new PrintStream(socket.getOutputStream(), true);
    }

    /**
     * Closes all the resources used up by network manager.
     *
     * @throws IOException When an error occurs while closing the resources.
     */
    public void Close() throws IOException{
        socket.close();
        intoSocketStream.close();
    }

    /**
     * Sends a given string over the network.
     *
     * @param msg The msg to be sent over the network.
     *
     * @throws IOException When sending the msg fails for some reason.
     */
    public void SendString(String msg) throws IOException {
        intoSocketStream.println(msg);
    }

    /**
     * Reads the string from the socket.

    public String GetString() {
    }
     */
}

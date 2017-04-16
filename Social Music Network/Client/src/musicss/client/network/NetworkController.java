package musicss.client.network;

import musicss.protocol.ProtocolImplementer;
import musicss.protocol.message.Request;
import musicss.protocol.message.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Singleton controller that manages all exterior connections to the application server
 */
public class NetworkController {
    public static NetworkController instance = new NetworkController();

    public boolean isConnected;

    private Socket socket;
    private PrintStream outputStream;
    private BufferedReader inputStream;
    private ProtocolImplementer protocol;

    private NetworkController() {
        instance = this;
        protocol = new ProtocolImplementer();
        socket = new Socket();
        isConnected = false;
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
    public void connect() {
        try {
            socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 9999));

            outputStream = new PrintStream(socket.getOutputStream());
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            isConnected = true;

            System.out.println("Successfully connected to the server.");
        } catch (IOException e) {
            System.err.println("ERROR: Couldn't connect to server or get stream\n\t" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Parses a given request and sends it over to the server, returns the response from the server.
     *
     * @param request The request to be sent over the network.
     *
     * @return The response for the request made.
     */
    public Response sendRequest(Request request) {
        String msg = protocol.pack(request);
        if (msg.equals(ProtocolImplementer.StatusCodes.VOID))
            System.out.println("This message has been identified as VOID");
        Response response = new Response.Void();

        if (outputStream != null)
        {
            outputStream.println(msg);

            response = getResponse();
        }

        return response;
    }

    /**
     * Returns the response that just arrived.
     *
     * @return The response from the server
     */
    public Response getResponse() {
        Response response = new Response.Void();

        try {
            String msg = inputStream.readLine();
            if (msg == null) {
                // the connection has been broken
                isConnected = false;
            } else {
                response = protocol.unpackResponse(msg);

                if (response.type == Response.Types.VOID)
                    System.out.println("This response has been identified as VOID");
            }
        } catch (IOException e) {
            System.err.println("ERROR: Couldn't read response" + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
}

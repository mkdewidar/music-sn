package com.smn.app.client.network;

import com.smn.app.client.scene.SceneController;

import com.smn.app.protocol.ProtocolImplementer;
import com.smn.app.protocol.message.Request;
import com.smn.app.protocol.message.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Singleton controller that manages all exterior connections to the application server
 */
public class NetworkController {
    public static NetworkController instance = new NetworkController();

    public SceneController sceneController;

    private boolean isConnected;

    private Socket socket;
    private PrintStream outputStream;
    private BufferedReader inputStream;
    private ProtocolImplementer protocol;

    private Thread listenerThread;

    private NetworkController() {
        instance = this;
        protocol = new ProtocolImplementer();
        socket = new Socket();

        listenerThread = new Thread(new ListenerThread());
    }

    /**
     * A Runnable which listens for any messages from the server and notifies the
     * controller when they arrive.
     */
    private class ListenerThread implements Runnable {
        @Override
        public void run() {
            String msg = "";
            while (msg != null) {
                try {
                    msg = inputStream.readLine();

                    Response serverEvent =  protocol.unpackResponse(msg);
                    if (serverEvent.type == Response.Types.VOID)
                        System.err.println("ERROR: This server event has been identified as VOID.");

                    sceneController.handleServerEvent(serverEvent);
                } catch (IOException e) {
                    System.err.println("ERROR: Lost connection with server\n\t" + e.getMessage());
                    break;
                }
            }
            isConnected = false;
            sceneController.setNetworkConnected(false);
        }
    }

    public boolean isConnected() {
        return isConnected;
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
            if (inputStream != null) {
                inputStream.close();
            }
            listenerThread.interrupt();
        } catch (IOException e) {
            System.err.println("ERROR: An IO error occurred while closing resources\n\t" + e.getMessage());
        }
    }

    /**
     * Connects the controller to the server.
     */
    public void connect() {
        try {
            socket = new Socket(InetAddress.getLocalHost(), 9999);

            outputStream = new PrintStream(socket.getOutputStream());
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            isConnected = true;
            sceneController.setNetworkConnected(true);

            listenerThread.start();
            System.out.println("Successfully connected to the server.");
        } catch (IOException e) {
            System.err.println("ERROR: Couldn't connect to server or get stream\n\t" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Parses a given clientEvent and sends it over to the server, returns the response from the server.
     *
     * @param clientEvent The clientEvent to be sent over the network.
     *
     * @return The response for the clientEvent made.
     */
    public void sendRequest(Request clientEvent) {
        String msg = protocol.pack(clientEvent);
        if (msg.equals(ProtocolImplementer.StatusCodes.VOID))
            System.err.println("ERROR: This client event is VOID");

        // It can be null if we never connected to the server to begin with
        if (outputStream != null)
        {
            outputStream.println(msg);
        }
    }
}

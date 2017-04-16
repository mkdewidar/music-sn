package musicss.server;

import musicss.protocol.message.Request;
import musicss.protocol.message.Response;
import musicss.protocol.ProtocolImplementer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Represents a thread that serves one single client.
 */
public class ServerWorker implements Runnable {
    private Socket socket;
    private BufferedReader socketReader;
    private PrintStream socketPrintStream;
    // The message obtained from or to be sent to the client, it's already in it's packed form
    private String packedMessage;
    private ProtocolImplementer protocol;

    public ServerWorker(Socket clientSocket) throws IOException {
        ServeClient(clientSocket);
        protocol = new ProtocolImplementer();
    }

    /**
     * Provides a client socket which the worker thread can use to serve the client.
     *
     * @throws IOException When the streams needed for socket use can't be obtained.
     */
    public void ServeClient(Socket clientSocket) throws IOException {
        socket = clientSocket;
        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        socketPrintStream = new PrintStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        System.out.println("THREAD #" + Thread.currentThread().getId() + " serving client "
                + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

        while (true) {
            System.out.print("Thread #" + Thread.currentThread().getId());
            System.out.println(", msg from " + socket.getInetAddress().getHostAddress());

            try {
                packedMessage = socketReader.readLine();
                // null is the only way we know we have been disconnected, it's not a message
                if (packedMessage == null)
                    break;
            } catch (IOException e) {
                System.err.println("ERROR: Couldn't read from user stream, connection may be lost " + e.getMessage());
                break;
            }

            Request clientRequest = protocol.unpackRequest(packedMessage);

            Response response = new Response.InvalidAuth();

            packedMessage = protocol.pack(response);
            socketPrintStream.println(packedMessage);
        }

        System.out.println("THREAD " + Thread.currentThread().getId() + " quitting...");
    }
}

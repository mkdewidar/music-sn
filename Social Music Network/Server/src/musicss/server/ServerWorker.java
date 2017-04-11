package musicss.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Represents a thread that serves one single client.
 */
public class ServerWorker implements Runnable {
    private Socket socket;
    private BufferedReader socketReader;

    public ServerWorker() {
    }

    public ServerWorker(Socket clientSocket) throws IOException {
        ServeClient(clientSocket);
    }

    /**
     * Provides a client socket which the worker thread can use to serve the client.
     *
     * @throws IOException When the streams needed for socket use can't be obtained.
     */
    public void ServeClient(Socket clientSocket) throws IOException {
        socket = clientSocket;
        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        System.out.println("THREAD #" + Thread.currentThread().getId() + " serving client "
                + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

        while (true) {

            try {
                System.out.print("Thread #" + Thread.currentThread().getId());
                System.out.println(", msg from " + socket.getInetAddress().getHostAddress());

                String userMsg = socketReader.readLine();
                if (userMsg == null) {
                    break;
                }
                System.out.println("\t" + userMsg);
            } catch (IOException e) {
                System.err.println("ERROR: Couldn't read from user stream, connection may be lost " + e.getMessage());
                break;
            }
        }

        System.out.println("THREAD " + Thread.currentThread().getId() + " quitting...");
    }
}

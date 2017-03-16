package musicss.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Manages all exterior connections to the application server
 */
public class ConnectionController implements Initializable {
    public static ConnectionController connectionController;
    private Socket socket;
    private PrintStream intoSocketStream;
    private Parent statusBanner;
    private boolean isConnected;

    @FXML
    private Button reconnectButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statusBanner = reconnectButton.getParent();
        this.SetIsConnected(false);
        socket = new Socket();

        this.Connect();

        reconnectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                connectionController.Connect();
            }
        });

        connectionController = this;
    }

    /**
     * Closes all the resources used up by controller.
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
     * Connects the controller to the server.
     */
    public void Connect() {
        try {
            socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 9999));
            intoSocketStream = new PrintStream(socket.getOutputStream());

            this.SetIsConnected(true);

            System.out.println("Successfully connected to the server.");
        } catch (IOException e) {
            System.err.println("ERROR: Couldn't connect to server\n\t" + e.getMessage());
            e.printStackTrace();
            this.SetIsConnected(false);
        }
    }

    /**
     * Returns whether or not the controller is connected to the server and ready
     */
    public boolean IsConnected() {
        return  isConnected;
    }

    private void SetIsConnected(boolean status) {
        isConnected = status;
        statusBanner.setManaged(!status);
        statusBanner.setVisible(!status);
    }

    /**
     * Sends a given string over the network. You must check the connection is valid
     *      before attempting to send anything.
     *
     * @param msg The msg to be sent over the network.
     */
    public void SendString(String msg) {
        if (intoSocketStream != null) {
            intoSocketStream.println(msg);
        }
    }

    /**
     * Reads the string from the socket.

    public String GetString() {
    }
     */
}

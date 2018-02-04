package com.smn.app.client.scene;

import com.smn.app.client.control.StatusControl;
import com.smn.app.client.network.NetworkController;

import com.smn.app.protocol.message.ClientEvent;
import com.smn.app.protocol.message.ServerEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Abstract base class for all scene controllers, consists of a status bar and the required interface for it.
 * Also has a reference to the network interface which allows any controller to send and receive messages from
 *      the server.
 */
public abstract class SceneController implements Initializable {
    @FXML
    protected StatusControl statusBanner;
    @FXML
    protected VBox rootNode;

    protected NetworkController networkController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        networkController = NetworkController.instance;
        setNetworkConnected(networkController.isConnected());
        networkController.sceneController = this;

        statusBanner.setOnReconnect((event) -> {
            networkController.connect();
        });
    }

    /**
     * Abstract function called by the network controller whenever an event arrives from the server.
     * @param event The event that arrived from the server.
     */
    public abstract void handleServerEvent(ServerEvent event);

    /**
     * Tells the status banner of the status of the connection. Called by network controller
     *      and also by the class.
     * @param connected Whether or not we are connected to the network.
     */
    public void setNetworkConnected(boolean connected) {
        statusBanner.setConnected(connected);
    }
}

package com.smn.app.client.scene;

import com.smn.app.client.control.StatusControl;
import com.smn.app.client.network.NetworkController;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Base class for all scene controllers, consists of a status bar and the required interface for it.
 * Also has a reference to the network interface which allows any controller to send and receive messages from
 *      the server.
 */
public class SceneController implements Initializable {
    protected StatusControl statusBanner;
    protected NetworkController networkController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statusBanner = new StatusControl();
        networkController = NetworkController.instance;
        // Updates the status banner as per the is connected variable
        networkController.isConnected.addListener((event) -> {
            statusBanner.setConnected(networkController.isConnected.get());
        });
    }
}
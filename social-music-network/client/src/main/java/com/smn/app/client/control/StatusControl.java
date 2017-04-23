package com.smn.app.client.control;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.io.IOException;

/**
 * A status banner that shows the network is disconnected and provides a reconnect button
 */
public class StatusControl extends HBox {
    @FXML
    protected Button reconnectButton;

    public StatusControl() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/StatusControl.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            System.err.println("ERROR: Couldn't load the Status fxml file\n\t" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setOnReconnect(EventHandler eventHandler) {
        reconnectButton.setOnAction(eventHandler);
    }

    public void setConnected(boolean connected) {
        this.setVisible(!connected);
        this.setManaged(!connected);
    }
}

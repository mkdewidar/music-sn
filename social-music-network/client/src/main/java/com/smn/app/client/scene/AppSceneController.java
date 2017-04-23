package com.smn.app.client.scene;

import com.smn.app.client.control.FriendsControl;
import com.smn.app.protocol.message.ServerEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for the main application scene.
 */
public class AppSceneController extends SceneController {
    @FXML
    protected VBox rootNode;
    @FXML
    protected FriendsControl friendsControl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        friendsControl.setOnFriendSearch((event) -> {
            System.out.println("Searching for friend!");
        });
    }

    @Override
    public void handleServerEvent(ServerEvent event) {
    }
}

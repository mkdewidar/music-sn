package com.smn.app.client.scene;

import com.smn.app.client.control.FriendsControl;
import com.smn.app.protocol.message.ClientEvent;
import com.smn.app.protocol.message.ServerEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
            searchForUser(friendsControl.getSearchText());
        });

        networkController.sendRequest(new ClientEvent.FriendsList());
    }

    @Override
    public void handleServerEvent(ServerEvent event) {
        switch (event.type) {
            case USERFRIENDS:
                ServerEvent.UserFriends userFriends = (ServerEvent.UserFriends) event;
                List friendList = new ArrayList();
                if (userFriends.friends[0].equals("")) {
                    friendList.add("No Friends Yet!");
                } else {
                    friendList = Arrays.asList(userFriends.friends);
                }
                friendsControl.setFriendsList(friendList);
                break;
        }
    }

    private void searchForUser(String searchString) {
    }
}

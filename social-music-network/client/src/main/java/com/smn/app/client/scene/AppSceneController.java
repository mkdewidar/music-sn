package com.smn.app.client.scene;

import com.smn.app.client.control.FriendsControl;
import com.smn.app.protocol.message.ClientEvent;
import com.smn.app.protocol.message.ServerEvent;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
        friendsControl.setOnFriendRequest((event) -> {
            // Traverses the tree structure of the cell to get the label containing the username
            Label usernameLabel = (Label) ((Button) event.getTarget()).getParent().getChildrenUnmodifiable().get(0);

            sendFriendRequest(usernameLabel.getText());
        });

        networkController.sendRequest(new ClientEvent.FriendsList());
    }

    @Override
    public void handleServerEvent(ServerEvent event) {
        switch (event.type) {
            case USERFRIENDS:
                ServerEvent.UserFriends userFriends = (ServerEvent.UserFriends) event;
                ArrayList<String> friendList = new ArrayList<>();
                if (userFriends.friends.length == 0) {
                    friendList.add("No Friends Yet!");
                } else {
                    friendList.addAll(Arrays.asList(userFriends.friends));
                }

                Platform.runLater(() -> {
                    friendsControl.setFriendsList(friendList);
                });
                break;
            case USERSEARCH:
                ServerEvent.UserSearch userSearch = (ServerEvent.UserSearch) event;
                ArrayList<String> results = new ArrayList();
                results.addAll(Arrays.asList(userSearch.results));

                Platform.runLater(() -> {
                    friendsControl.setSearchResults(results);
                });
                break;
        }
    }

    private void searchForUser(String searchString) {
        ClientEvent.UserSearch userSearch = new ClientEvent.UserSearch();
        userSearch.searchString = searchString;

        this.networkController.sendRequest(userSearch);
    }

    private void sendFriendRequest(String receiver) {
        ClientEvent.FriendRequest friendRequest = new ClientEvent.FriendRequest();
        friendRequest.username = receiver;

        this.networkController.sendRequest(friendRequest);
    }
}

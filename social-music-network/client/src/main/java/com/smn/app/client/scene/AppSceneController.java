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

    protected String username;
    protected String password;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        // This completely overrides the parent classes handler, so we must do what it does as well
        statusBanner.setOnReconnect((event) -> {
            this.networkController.connect();

            ClientEvent.Login login = new ClientEvent.Login();
            login.username = username;
            login.password = password;

            this.networkController.sendRequest(login);
        });

        friendsControl.setOnFriendSearch((event) -> {
            searchForUser(friendsControl.getSearchText());
        });
        friendsControl.setOnCellActions((event) -> {
            // -- On Friend Request -- //
            // Traverses the tree structure of the cell to get the label containing the username
            // in this case that is the second child to Hbox
            Label usernameLabel = (Label) ((Button) event.getTarget()).getParent().getChildrenUnmodifiable().get(3);

            sendFriendRequest(usernameLabel.getText());
        }, (event) -> {
            // -- On Accept Friend Request -- //
            // The third child of the Hbox is the label in this case
            Label usernameLabel = (Label) ((Button) event.getTarget()).getParent().getChildrenUnmodifiable().get(3);

            friendRequestReply(usernameLabel.getText(), true);
        }, (event) -> {
            // -- On Reject Friend Request -- //
            // The third child of the Hbox is the label in this case
            Label usernameLabel = (Label) ((Button) event.getTarget()).getParent().getChildrenUnmodifiable().get(3);

            friendRequestReply(usernameLabel.getText(), false);
        });

        networkController.sendRequest(new ClientEvent.FriendsList());
    }

    /**
     * Called by the network controller whenever a message is received from the server.
     * @param event The event that arrived from the server.
     */
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
                ArrayList<String> requestList = new ArrayList<>();
                if (userFriends.requests.length != 0) {
                    requestList.addAll(Arrays.asList(userFriends.requests));
                }

                Platform.runLater(() -> {
                    friendsControl.setFriendsList(friendList);
                    friendsControl.setFriendRequests(requestList);
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

    /**
     * Sets the details of the current logged in user.
     * @param id The id of the current logged in user.
     * @param pass The pass of the current logged in user.
     */
    public void setUserDetails(String id, String pass) {
        username = id;
        password = pass;
    }

    /**
     * Sends a request to search by the search criteria provided.
     * @param searchString The search criteria to search by in regex.
     */
    private void searchForUser(String searchString) {
        ClientEvent.UserSearch userSearch = new ClientEvent.UserSearch();
        userSearch.searchString = searchString;

        this.networkController.sendRequest(userSearch);
    }

    /**
     * Send a friend request to another user.
     * @param receiver The person receiving the friend request.
     */
    private void sendFriendRequest(String receiver) {
        ClientEvent.FriendRequest friendRequest = new ClientEvent.FriendRequest();
        friendRequest.username = receiver;

        this.networkController.sendRequest(friendRequest);
    }

    /**
     * Sends a friend request reply to the server.
     * @param receiver The one receiving the reply, aka the sender of the friend request.
     * @param accept Whether it's an accept or a reject.
     */
    private void friendRequestReply(String receiver, boolean accept) {
        ClientEvent.FriendRequestReply reply = new ClientEvent.FriendRequestReply();

        reply.sender = receiver;
        reply.accept = accept;

        this.networkController.sendRequest(reply);
    }
}

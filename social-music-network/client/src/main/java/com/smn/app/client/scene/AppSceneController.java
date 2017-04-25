package com.smn.app.client.scene;

import com.smn.app.client.control.ChannelControl;
import com.smn.app.client.control.FriendsControl;
import com.smn.app.client.event.AppEvent;
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
    @FXML
    protected ChannelControl channelControl;

    // The type of the last made request, helps determine how to react to the current server event
    // if there are many ways to reply to it e.g what to do with a server OK event.
    protected ClientEvent.Types lastMadeRequest;

    protected String username;
    protected String password;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        // This completely overrides the parent classes handler, so we must do the connect ourselves
        statusBanner.setOnReconnect((event) -> {
            this.networkController.connect();

            ClientEvent.Login login = new ClientEvent.Login();
            login.username = username;
            login.password = password;

            this.networkController.sendClientEvent(login);
            lastMadeRequest = login.type;
        });

        friendsControl.setOnFriendSearch((event) -> {
            searchForUser(friendsControl.getSearchText());
        });
        friendsControl.setOnCellActions((event) -> {
            // -- On Friend Request -- //
            // Traverses the tree structure of the cell to get the label containing the creator
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

        channelControl.setOnCreateChannel((event) -> {
            createChannel(channelControl.createChannelControl.getChannelName(),
                    channelControl.createChannelControl.getChannelMembers());
        });
        channelControl.setOnChannelSelected((observable, oldValue, newValue) -> {
        });

        this.networkController.sendClientEvent(new ClientEvent.FriendsList());
        lastMadeRequest = ClientEvent.Types.FRIENDSLIST;

        this.networkController.sendClientEvent(new ClientEvent.ChannelList());
        lastMadeRequest = ClientEvent.Types.CHANNELLIST;
    }

    /**
     * Called by the network controller whenever a message is received from the server.
     * @param event The event that arrived from the server.
     */
    @Override
    public void handleServerEvent(ServerEvent event) {
        switch (event.type) {
            case USERFRIENDS: {
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
            }

            case USERSEARCH: {
                ServerEvent.UserSearch userSearch = (ServerEvent.UserSearch) event;
                ArrayList<String> results = new ArrayList();
                results.addAll(Arrays.asList(userSearch.results));

                Platform.runLater(() -> {
                    friendsControl.setSearchResults(results);
                });
                break;
            }

            case USERCHANNELS: {
                ServerEvent.UserChannels userChannels = (ServerEvent.UserChannels) event;

                ArrayList<String> channels = new ArrayList<>();
                if (userChannels.channels.length == 0) {
                    // The "a_" is necessary due to the structure of a channel id
                    channels.add("a_No Channels yet!");
                } else {
                    channels.addAll(Arrays.asList(userChannels.channels));
                }

                Platform.runLater(() -> {
                    channelControl.setChannelList(channels);
                });
                break;
            }
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
    protected void searchForUser(String searchString) {
        ClientEvent.UserSearch userSearch = new ClientEvent.UserSearch();
        userSearch.searchString = searchString;

        this.networkController.sendClientEvent(userSearch);
        lastMadeRequest = userSearch.type;
    }

    /**
     * Send a friend request to another user.
     * @param receiver The person receiving the friend request.
     */
    protected void sendFriendRequest(String receiver) {
        ClientEvent.FriendRequest friendRequest = new ClientEvent.FriendRequest();
        friendRequest.username = receiver;

        this.networkController.sendClientEvent(friendRequest);
        lastMadeRequest = friendRequest.type;
    }

    /**
     * Sends a friend request reply to the server.
     * @param receiver The one receiving the reply, aka the sender of the friend request.
     * @param accept Whether it's an accept or a reject.
     */
    protected void friendRequestReply(String receiver, boolean accept) {
        ClientEvent.FriendRequestReply reply = new ClientEvent.FriendRequestReply();

        reply.sender = receiver;
        reply.accept = accept;

        this.networkController.sendClientEvent(reply);
        lastMadeRequest = reply.type;
    }

    /**
     * Sends a request to create a new channel.
     */
    protected void createChannel(String channelName, String[] memberNames) {
        ClientEvent.CreateChannel createChannel = new ClientEvent.CreateChannel();
        createChannel.creator = username;
        createChannel.channelName = channelName;
        createChannel.members = memberNames;

        this.networkController.sendClientEvent(createChannel);
    }

    /**
     * Function used by the menu bar to close.
     */
    @FXML
    protected void closeApplication() {
        Platform.exit();
    }

    /**
     * Function used in the menu bar to logout
     */
    @FXML
    protected void logout() {
        rootNode.fireEvent(new AppEvent.Logout());
    }
}

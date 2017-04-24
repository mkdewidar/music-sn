package com.smn.app.client.control;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A custom control that manages the users list of friends.
 */
public class FriendsControl extends VBox {
    @FXML
    protected TextField friendSearchField;
    @FXML
    protected ListView<FriendListItem> friendListView;

    // The list of all the user friends
    protected ArrayList<FriendListItem> friends;
    // The actual items being viewed by the listview
    protected ObservableList<FriendListItem> listItems;

    public FriendsControl() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FriendsControl.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            System.err.println("ERROR: Couldn't load the FriendsControl fxml file\n\t" + e.getMessage());
            e.printStackTrace();
            Platform.exit();
            return;
        }

        friends = new ArrayList<>();
        friends.add(new FriendListItem("Loading friends...", true));

        listItems = FXCollections.observableArrayList();
        listItems.addAll(friends);

        friendListView.setItems(listItems);

        // sets the list to the user's friends when not searching
        friendSearchField.setOnKeyReleased((event) -> {
            if (getSearchText().equals("")) {
                listItems.setAll(friends);
            }
        });
    }

    public void setOnFriendRequest(EventHandler eventHandler) {
        friendListView.setCellFactory((param) -> {
            FriendsListCell newCell = new FriendsListCell(eventHandler);
            return newCell;
        });
    }

    /**
     * Sets the items in the friends list. Also sets current viewable list to friends list.
     * @param items The items to replace the ones in the current list.
     */
    public void setFriendsList(List<String> items) {
        friends.clear();

        for (String item : items) {
            friends.add(new FriendListItem(item, true));
        }

        listItems.setAll(friends);
    }

    /**
     * Sets an event handler to be called when the user attempts to search for someone.
     * @param eventHandler The handler to be called.
     */
    public void setOnFriendSearch(EventHandler eventHandler) {
        friendSearchField.setOnAction(eventHandler);
    }

    public String getSearchText() {
        return friendSearchField.getText();
    }

    /**
     * Set's the results of a search and automatically views it.
     * @param items The list of search results.
     */
    public void setSearchResults(List<String> items) {
        listItems.clear();
        for (String item : items) {
            listItems.add(new FriendListItem(item, false));
        }
    }

    /**
     * Represents information about a single friend item in the list.
     */
    public static class FriendListItem {
        public String username;
        public boolean isFriend;

        public FriendListItem(String name, boolean friend) {
            username = name;
            isFriend = friend;
        }
    }

    /**
     * Represents an actual cell in the friends list control.
     */
    private class FriendsListCell extends ListCell<FriendListItem> {
        private HBox cellRoot;
        private Label username;
        private Button sendRequest;

        public FriendsListCell(EventHandler onFriendRequest) {
            cellRoot = new HBox();
            username = new Label();
            sendRequest = new Button();
            sendRequest.setText("Send Request");

            sendRequest.setOnAction(onFriendRequest);

            cellRoot.getChildren().addAll(username, sendRequest);
        }

        @Override
        public void updateItem(FriendListItem item, boolean empty) {
            super.updateItem(item, empty);

            setText(null);
            if (empty || (item == null)) {
                setGraphic(null);
            } else {
                username.setText(item.username);
                if (item.isFriend) {
                    sendRequest.setVisible(false);
                    sendRequest.setManaged(false);
                } else {
                    sendRequest.setVisible(true);
                    sendRequest.setManaged(true);
                }

                setGraphic(cellRoot);
            }
        }
    }
}

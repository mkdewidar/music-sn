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
        friends.add(new FriendListItem("Loading friends...", CellType.FRIEND));

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

    public void setOnCellActions(EventHandler onFriendRequest, EventHandler onAccept, EventHandler onReject) {
        friendListView.setCellFactory((param) -> {
            FriendsListCell newCell = new FriendsListCell(onFriendRequest, onAccept, onReject);
            return newCell;
        });
    }

    public void setFriendRequests(List<String> items) {
        for (String item : items) {
            friends.add(new FriendListItem(item, CellType.REQUEST));
        }

        listItems.setAll(friends);
    }

    /**
     * Sets the items in the friends list. Also sets current viewable list to friends list.
     * @param items The items to replace the ones in the current list.
     */
    public void setFriendsList(List<String> items) {
        friends.clear();

        for (String item : items) {
            friends.add(new FriendListItem(item, CellType.FRIEND));
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
            listItems.add(new FriendListItem(item, CellType.STRANGER));
        }
    }

    /**
     * Represents information about a single friend item in the list.
     */
    public static class FriendListItem {
        public String username;
        public CellType type;

        public FriendListItem(String name, CellType cellType) {
            username = name;
            type = cellType;
        }
    }

    public enum CellType {
        REQUEST,
        FRIEND,
        STRANGER
    }

    /**
     * Represents an actual cell in the friends list control.
     */
    private class FriendsListCell extends ListCell<FriendListItem> {
        private HBox cellRoot;
        private Label username;

        private Button sendRequest;
        private Button acceptRequest;
        private Button rejectRequest;

        public FriendsListCell(EventHandler onFriendRequest, EventHandler onAcceptRequest, EventHandler onRejectRequest) {
            cellRoot = new HBox();
            username = new Label();

            sendRequest = new Button();
            sendRequest.setText("+");
            sendRequest.setOnAction(onFriendRequest);

            acceptRequest = new Button();
            acceptRequest.setText("Yes");
            acceptRequest.setOnAction(onAcceptRequest);

            rejectRequest = new Button();
            rejectRequest.setText("No");
            rejectRequest.setOnAction(onRejectRequest);

            cellRoot.getChildren().addAll(sendRequest, acceptRequest, rejectRequest, username);
            cellRoot.setSpacing(5.0);
        }

        @Override
        public void updateItem(FriendListItem item, boolean empty) {
            super.updateItem(item, empty);

            setText(null);
            if (empty || (item == null)) {
                setGraphic(null);
            } else {
                username.setText(item.username);
                switch (item.type) {
                    case STRANGER:
                        sendRequest.setVisible(true);
                        sendRequest.setManaged(true);

                        acceptRequest.setVisible(false);
                        acceptRequest.setManaged(false);

                        rejectRequest.setVisible(false);
                        rejectRequest.setVisible(false);
                        break;

                    case FRIEND:
                        sendRequest.setVisible(false);
                        sendRequest.setManaged(false);

                        acceptRequest.setVisible(false);
                        acceptRequest.setManaged(false);

                        rejectRequest.setVisible(false);
                        rejectRequest.setVisible(false);
                        break;

                    case REQUEST:
                        sendRequest.setVisible(false);
                        sendRequest.setManaged(false);

                        acceptRequest.setVisible(true);
                        acceptRequest.setManaged(true);

                        rejectRequest.setVisible(true);
                        rejectRequest.setVisible(true);
                        break;
                }

                setGraphic(cellRoot);
            }
        }
    }
}

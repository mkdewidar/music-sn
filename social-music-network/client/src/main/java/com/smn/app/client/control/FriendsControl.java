package com.smn.app.client.control;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
    protected ListView<String> friendListView;

    // The list of all the friends of the user
    protected ArrayList<String> friends;
    protected ObservableList<String> listItems;

    public FriendsControl() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FriendsControl.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            System.err.println("ERROR: Couldn't load the FriendsControl fxml filen\n\t" + e.getMessage());
            e.printStackTrace();
            Platform.exit();
            return;
        }

        friends = new ArrayList<String>();
        friends.add("Loading friends...");

        listItems = FXCollections.observableArrayList();
        listItems.setAll(friends);

        friendListView.setItems(listItems);

        // sets the list to the user's friends when not searching
        friendSearchField.setOnKeyReleased((event) -> {
            if (getSearchText().equals("")) {
                listItems.setAll(friends);
            }
        });
    }

    /**
     * Set's the results of a search and automatically views it.
     * @param items The list of search results.
     */
    public void setSearchResults(List items) {
        listItems.setAll(items);
    }

    /**
     * Sets an event handler to be called when the user attempts to search for someone.
     * @param eventHandler The handler to be called.
     */
    public void setOnFriendSearch(EventHandler eventHandler) {
        friendSearchField.setOnAction(eventHandler);
    }

    /**
     * Sets the items in the friends list. Also sets current viewable list to friends list.
     * @param items The items to replace the ones in the current list.
     */
    public void setFriendsList(List items) {
        friends.clear();
        friends.addAll(items);
        listItems.setAll(friends);
    }

    /**
     * Adds the items to the current items in the friends list. Also sets current viewable list to friends list.
     * @param items The items to be added to the list.
     */
    public void addFirendsListItems(List items) {
        friends.addAll(items);
        listItems.setAll(friends);
    }

    public String getSearchText() {
        return friendSearchField.getText();
    }
}

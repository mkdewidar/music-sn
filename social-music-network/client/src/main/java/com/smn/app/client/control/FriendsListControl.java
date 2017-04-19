package com.smn.app.client.control;

import com.smn.app.client.scene.AppSceneController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

/**
 * A custom control that manages the users list of friends.
 */
public class FriendsListControl extends ListView<String> {
    protected AppSceneController controller;
    protected ObservableList<String> items;

    public FriendsListControl(AppSceneController sceneController) {
        controller = sceneController;

        items = FXCollections.observableArrayList();
        items.addAll("One", "Two", "Three", "Four");

        setItems(items);
    }
}

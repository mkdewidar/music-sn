package com.smn.app.client.control;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

/**
 * Custom control for channel management.
 */
public class ChannelControl extends VBox {
    @FXML
    public CreateChannelControl createChannelControl;
    @FXML
    protected ListView<ChannelListItem> channelsListView;

    protected ObservableList<ChannelListItem> channelList;

    public ChannelControl() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChannelControl.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
        } catch (IOException e) {
            System.err.println("ERROR: Couldn't load the ChannelControl fxml\n\t" + e.getMessage());
            Platform.exit();
            return;
        }

        channelList = FXCollections.observableArrayList();
        // The "a_" is necessary due to the structure of a channel id
        channelList.add(new ChannelListItem("a_Loading channels..."));

        channelsListView.setItems(channelList);
        channelsListView.setCellFactory((event) -> {
            return new ChannelListView();
        });
    }

    /**
     * Sets the current list of channels to the channels provided.
     * @param channelIds Array of string id's that the list should be set to.
     */
    public void setChannelList(List<String> channelIds) {
        channelList.clear();

        for (String channel : channelIds) {
            // The first element is the creator of the channel and the second is the name
            channelList.add(new ChannelListItem(channel));
        }
    }

    /**
     * Sets an event handler for when the create button is pressed in the channel control.
     * @param eventHandler The event handler to be called when the create channel event occurs
     *                     in the create channel control.
     */
    public void setOnCreateChannel(EventHandler<ActionEvent> eventHandler) {
        createChannelControl.setOnCreateChannel(eventHandler);
    }

    /**
     * Sets a handler to be called whenever a new item is selected from the list.
     * @param eventHandler The handler to be called when an item is selected.
     */
    public void setOnChannelSelected(ChangeListener eventHandler) {
        channelsListView.getSelectionModel().selectedItemProperty().addListener(eventHandler);
    }

    /**
     * The structure of the information being held per item in the list of channels.
     */
    public class ChannelListItem {
        public String name;
        public String id;

        public ChannelListItem(String channelId) {
            name = channelId.split("_")[1];
            id = channelId;
        }
    }

    /**
     * Defines a viewable channel list item.
     */
    private class ChannelListView extends ListCell<ChannelListItem> {
        @Override
        protected void updateItem(ChannelListItem item, boolean empty) {
            super.updateItem(item, empty);

            setGraphic(null);
            if ((empty != true) || (item != null)) {
                setText(item.name);
            } else {
                setText(null);
            }
        }
    }
}

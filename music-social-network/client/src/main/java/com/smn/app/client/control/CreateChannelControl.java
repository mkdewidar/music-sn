package com.smn.app.client.control;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * The form that manages creating the channel.
 */
public class CreateChannelControl extends Accordion {
    @FXML
    protected Label uniqueNameErrorLabel;
    @FXML
    protected Label noNameErrorLabel;
    @FXML
    protected TextField channelNameField;
    @FXML
    protected TextField channelMembersField;
    @FXML
    protected Button createChannelButton;

    public CreateChannelControl() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreateChannelControl.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
        } catch (IOException e) {
            System.err.println("ERROR: Couldn't load the CreateChannelControl fxml\n\t" + e.getMessage());
            Platform.exit();
            return;
        }

        setInvalidName(false);
        setNoNameError(false);

        // A filter that checks the sanity of the fields
        createChannelButton.addEventFilter(ActionEvent.ACTION, (event) -> {
            if (channelNameField.getText().equals("")) {
                setNoNameError(true);
                event.consume();
            }
            if (channelNameField.getText().contains("_")) {
                // TODO: show error asking for name without underscore
                event.consume();
            }
        });
    }

    /**
     * Shows the error message saying the channel name is invalid.
     * @param invalid Whether or not the name is invalid, set true if invalid.
     */
    public void setInvalidName(boolean invalid) {
        uniqueNameErrorLabel.setManaged(invalid);
        uniqueNameErrorLabel.setVisible(invalid);
    }

    /**
     * Shows the error message saying the you must enter a channel name.
     * @param invalid Whether or not the name is invalid, set true if invalid.
     */
    public void setNoNameError(boolean invalid) {
        noNameErrorLabel.setManaged(invalid);
        noNameErrorLabel.setVisible(invalid);
    }

    /**
     * Sets an event handler for when the create channel button is called.
     * @param eventHandler The handler to be called when the event is fired.
     */
    public void setOnCreateChannel(EventHandler<ActionEvent> eventHandler) {
        createChannelButton.setOnAction(eventHandler);
    }

    /**
     * The name of the channel in the text field.
     * @return The string name of the channel as in the field.
     */
    public String getChannelName() {
        return channelNameField.getText();
    }

    /**
     * Returns names of the channel members
     * @return An array of the names of the channel members.
     */
    public String[] getChannelMembers() {
        return channelMembersField.getText().split(";");
    }
}

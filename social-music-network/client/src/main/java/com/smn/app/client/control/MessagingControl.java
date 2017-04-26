package com.smn.app.client.control;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Custom control for managing the messages for a channel.
 */
public class MessagingControl extends VBox {
    @FXML
    public ListView<Message> messagesListView;
    @FXML
    protected TextArea messageTextArea;
    @FXML
    protected Button sendMessageButton;

    protected ObservableList<Message> messages;
    // The message that was in the text area at the time of clicking the button
    public Message currentMessage;

    public MessagingControl() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MessagingControl.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
        } catch (IOException e) {
            System.err.println("ERROR: Couldn't load the MessagingControl fxml\n\t" + e.getMessage());
            Platform.exit();
            return;
        }

        messages = FXCollections.observableArrayList();
        currentMessage = new Message(null, null, null);

        messagesListView.setItems(messages);
        messagesListView.setCellFactory((param) -> {
            return new MessageListCell();
        });

        sendMessageButton.addEventFilter(ActionEvent.ACTION, (event) -> {
            if (messageTextArea.getText().equals("")) {
                event.consume();
            }
        });
        // this method of adding handlers is guaranteed to work before the ones the user sets
        // with our function
        sendMessageButton.addEventHandler(ActionEvent.ACTION, (event) -> {
            currentMessage.message = messageTextArea.getText();
            currentMessage.timestamp = new Date();
        });
    }

    /**
     * Sets an event handler to be called when the send message button is pressed.
     * @param eventHandler The event handler to be called.
     */
    public void setOnSendMessage(EventHandler eventHandler) {
        sendMessageButton.setOnAction(eventHandler);
    }

    /**
     * Adds the current message in the text field to the list.
     */
    public void addCurrentMessage() {
        messages.add(currentMessage);
    }

    /**
     * Sets the messages to be the current messages in the message control.
     * The most recent should be the last in the array.
     * The map should be a map of message field to the contents. For now that is:
     *      sender : String of senders username
     *      timestamp : Date of timestamp
     *      contents : String with contents of the message
     * @param msgs The messages to be set in the control
     */
    public void setMessages(Map<String, Object>[] msgs) {
        for (Map<String, Object> message : msgs) {
            messages.add(new Message((String) message.get("sender"),
                    (String) message.get("contents"),
                    (Date) message.get("timestamp")));
        }
    }

    /**
     * Structure representing a message.
     */
    public class Message {
        public String sender;
        public String message;
        public Date timestamp;

        public Message(String userSender, String contents, Date time) {
            sender = userSender;
            message = contents;
            timestamp = time;
        }
    }

    /**
     * The implementation for a single cell of the item list.
     */
    private class MessageListCell extends ListCell<Message> {
        private HBox root;
        private Label dateLabel;
        private Label senderLabel;
        private Label messageLabel;

        public MessageListCell() {
            root = new HBox();
            root.setSpacing(5.0);

            dateLabel = new Label();
            senderLabel = new Label();
            messageLabel = new Label();

            root.getChildren().setAll(dateLabel, senderLabel, messageLabel);
        }

        @Override
        public void updateItem(Message item, boolean empty) {
            super.updateItem(item, empty);

            if ((empty != true) || (item != null)) {
                dateLabel.setText(item.timestamp.toString());
                senderLabel.setText(item.sender);
                messageLabel.setText(item.message);

                setGraphic(root);
            } else {
                setText(null);
                setGraphic(null);
            }
        }
    }
}

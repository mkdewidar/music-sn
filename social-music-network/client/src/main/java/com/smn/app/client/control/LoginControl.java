package com.smn.app.client.control;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Custom UI control for a login form.
 */
public class LoginControl extends VBox {
    @FXML
    protected TextField usernameField;
    @FXML
    protected PasswordField passwordField;
    @FXML
    protected Button loginButton;
    @FXML
    protected Label invalidLoginLabel;

    /**
     * Constructor for the Login Control, it requires a @see musicss.client.scene.LoginSceneController as it
     *      depends on it to validate the login information.
     */
    public LoginControl() {
        // The fxml file has all the layout and structural information for the buttons and fields
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoginControl.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            System.err.println("ERROR: Couldn't load login fxml\n\t" + e.getMessage());
            e.printStackTrace();
            Platform.exit();
            return;
        }
    }

    public void addOnLogin(EventHandler eventHandler) {
        loginButton.setOnAction(eventHandler);
    }

    public void setInvalidLogin() {
        invalidLoginLabel.setVisible(true);
        invalidLoginLabel.setManaged(true);
    }

    /**
     * Accessor function to get the creator.
     * @return The creator of the user as a String.
     */
    public String getUsername() {
        return usernameField.getText();
    }

    /**
     * Accessor function to get the password
     * @return The password of the user as a String.
     */
    public String getPassword() {
        return passwordField.getText();
    }
}

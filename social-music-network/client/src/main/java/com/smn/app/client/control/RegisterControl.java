package com.smn.app.client.control;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Custom control for a register form.
 */
public class RegisterControl extends VBox {
    @FXML
    protected TextField nameField;
    @FXML
    protected TextField emailField;
    @FXML
    protected TextField usernameField;
    @FXML
    protected PasswordField passwordField;
    @FXML
    protected Button registerButton;

    public RegisterControl() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/RegisterControl.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            System.err.println("ERROR: Couldn't load register control fxml\n\t" + e.getMessage());
            e.printStackTrace();
            Platform.exit();
            return;
        }
    }

    public void addOnRegister(EventHandler eventHandler) {
        registerButton.setOnAction(eventHandler);
    }

    public String getName() {
        return nameField.getText();
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }
}

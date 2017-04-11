package musicss.client.control;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import musicss.client.NetworkController;

import java.io.IOException;

/**
 * Custom UI control for a login form.
 */
public class LoginControl extends VBox {
    @FXML
    protected TextField usernameField;
    @FXML
    protected TextField passwordField;
    @FXML
    protected Button loginButton;

    public LoginControl() {
        // The fxml file has all the layout and structural information for the buttons and fields
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginControl.fxml"));
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

        loginButton.setOnAction((event) -> {
            loginSubmit();
        });
        loginButton.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER)
                loginSubmit();
        });
    }

    protected void loginSubmit() {
        NetworkController.connectionController.send(usernameField.getText() + " " + passwordField.getText());

        usernameField.clear();
        passwordField.clear();
    }
}

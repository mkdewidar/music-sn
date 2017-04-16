package musicss.client.control;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import musicss.client.scene.LoginSceneController;

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

    protected LoginSceneController controller;

    /**
     * Constructor for the Login Control, it requires a @see musicss.client.scene.LoginSceneController as it
     *      depends on it to validate the login information.
     * @param loginController The login controller that this login control will depend on.
     */
    public LoginControl(LoginSceneController loginController) {
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

        controller = loginController;

        loginButton.setOnAction((event) -> {
            loginSubmit();
        });
        loginButton.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER)
                loginSubmit();
        });
    }

    /**
     * Logs in the user by asking the controller to validate the results.
     */
    protected void loginSubmit() {
        controller.validateLogin(usernameField.getText(), passwordField.getText());
    }

    /**
     * Accessor function to get the username.
     * @return The username of the user as a String.
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

package musicss.client;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static musicss.client.ConnectionController.connectionController;

/**
 * Controls the login screen of the interface.
 */
public class LoginController implements Initializable {
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usernameTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    passwordTextField.requestFocus();
                }
            }
        });
        passwordTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    System.out.println("Sending username: " + usernameTextField.getText()
                            + ", password: "+ passwordTextField.getText());

                    if (connectionController.IsConnected()) {
                        connectionController.SendString("Sending username: " + usernameTextField.getText()
                                + ", password: "+ passwordTextField.getText());
                    }

                    usernameTextField.clear();
                    passwordTextField.clear();
                }
            }
        });
    }
}

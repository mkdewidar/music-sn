package musicss.client;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static musicss.client.NetworkManager.connectionManager;

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
                if (event.getCode() == KeyCode.ENTER)
                {
                    passwordTextField.requestFocus();
                }
            }
        });
        passwordTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)
                {
                    System.out.println("Sending username: " + usernameTextField.getText()
                            + ", password: "+ passwordTextField.getText());

                    try {
                        connectionManager.SendString("Sending username: " + usernameTextField.getText()
                                + ", password: "+ passwordTextField.getText());
                    } catch (IOException e) {
                        System.err.println("ERROR: The msg couldn't be send due to connection issue\n\t" + e.getMessage());
                    }

                    usernameTextField.clear();
                    passwordTextField.clear();

                }
            }
        });

        System.out.println("The login screen has been initialised");
    }
}

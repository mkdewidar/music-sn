package musicss.client;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import musicss.client.event.LoginEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls the login content.
 */
public class LoginController implements Initializable {
    @FXML
    private VBox parentNode;
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
                    LoginEvent loginEvent = new LoginEvent(usernameTextField.getText(), passwordTextField.getText());
                    parentNode.fireEvent(loginEvent);

                    usernameTextField.clear();
                    passwordTextField.clear();
                }
            }
        });
        parentNode.addEventHandler(LoginEvent.LoginEventType, new EventHandler<LoginEvent>() {
            @Override
            public void handle(LoginEvent event) {
                System.out.println("The event was handled by the login controller");
            }
        });
    }
}

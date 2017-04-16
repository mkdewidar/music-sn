package musicss.client.scene;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import musicss.client.control.LoginControl;
import musicss.client.event.AppEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls and manages the login scene. This includes the login form as well as the register form.
 */
public class LoginSceneController extends SceneController {
    @FXML
    protected BorderPane rootNode;

    protected LoginControl loginForm;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        loginForm = new LoginControl(this);

        rootNode.setCenter(loginForm);
        rootNode.setTop(this.statusBanner);
    }

    public void validateLogin(String username, String password) {
        System.out.println("Of course your login is valid!");
        // TODO: send request to server and await validation response
        AppEvent.Login login = new AppEvent.Login();
        rootNode.fireEvent(login);
    }
}

package com.smn.app.client.scene;

import com.smn.app.client.control.LoginControl;
import com.smn.app.client.event.AppEvent;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

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

        Request.Login loginRequest = new Request.Login();
        loginRequest.username = username;
        loginRequest.password = password;

        Response response = this.networkController.sendRequest(loginRequest);

        if (response.type == Response.Types.OK) {
            AppEvent.Login login = new AppEvent.Login();
            rootNode.fireEvent(login);
        } else if (response.type == Response.Types.INVALIDAUTH) {
            loginForm.setInvalidLogin();
        }
    }
}

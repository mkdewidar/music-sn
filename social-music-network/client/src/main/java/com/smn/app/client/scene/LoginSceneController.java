package com.smn.app.client.scene;

import com.smn.app.client.control.LoginControl;
import com.smn.app.client.control.RegisterControl;
import com.smn.app.client.event.AppEvent;

import com.smn.app.protocol.message.Request;
import com.smn.app.protocol.message.Response;

import javafx.application.Platform;
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
    @FXML
    protected LoginControl loginForm;
    @FXML
    protected RegisterControl registerForm;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        rootNode.setTop(statusBanner);

        loginForm.addOnLogin((event) -> {
            validateLogin(loginForm.getUsername(), loginForm.getPassword());
        });
        registerForm.addOnRegister((event) -> {
            registerUser(registerForm.getName(), registerForm.getUsername(),
                    registerForm.getPassword(), registerForm.getEmail());
        });
    }

    public void validateLogin(String username, String password) {
        Request.Login loginRequest = new Request.Login();
        loginRequest.username = username;
        loginRequest.password = password;

        this.networkController.sendRequest(loginRequest);
    }

    public void registerUser(String name, String userId, String pass, String email) {
        Request.Register registerRequest = new Request.Register();
        registerRequest.username = userId;
        registerRequest.password = pass;
        registerRequest.email = email;
        registerRequest.name = name;

        this.networkController.sendRequest(registerRequest);
    }

    @Override
    public void handleServerEvent(Response event) {
        switch (event.type) {
            case OK:
                Platform.runLater(() -> {
                    AppEvent.Login login = new AppEvent.Login();
                    rootNode.fireEvent(login);
                });
                break;
            case INVALIDAUTH:
                loginForm.setInvalidLogin();
                break;
            case INVALIDREG:
                registerForm.setInvalidReg();
                break;
        }
    }
}

package com.smn.app.client.scene;

import com.smn.app.client.control.LoginControl;
import com.smn.app.client.control.RegisterControl;
import com.smn.app.client.event.AppEvent;

import com.smn.app.protocol.message.ClientEvent;
import com.smn.app.protocol.message.ServerEvent;

import javafx.application.Platform;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls and manages the login scene. This includes the login form as well as the register form.
 */
public class LoginSceneController extends SceneController {
    @FXML
    protected LoginControl loginForm;
    @FXML
    protected RegisterControl registerForm;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        loginForm.addOnLogin((event) -> {
            validateLogin(loginForm.getUsername(), loginForm.getPassword());
        });
        registerForm.addOnRegister((event) -> {
            registerUser(registerForm.getName(), registerForm.getUsername(),
                    registerForm.getPassword(), registerForm.getEmail());
        });
    }

    public void validateLogin(String username, String password) {
        ClientEvent.Login loginRequest = new ClientEvent.Login();
        loginRequest.username = username;
        loginRequest.password = password;

        this.networkController.sendClientEvent(loginRequest);
    }

    public void registerUser(String name, String userId, String pass, String email) {
        ClientEvent.Register registerRequest = new ClientEvent.Register();
        registerRequest.username = userId;
        registerRequest.password = pass;
        registerRequest.email = email;
        registerRequest.name = name;

        this.networkController.sendClientEvent(registerRequest);
    }

    @Override
    public void handleServerEvent(ServerEvent event) {
        switch (event.type) {
            case OK:
                Platform.runLater(() -> {
                    AppEvent.Login login = new AppEvent.Login();
                    login.username = loginForm.getUsername();
                    login.password = loginForm.getPassword();
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

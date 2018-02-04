package com.smn.app.client;

import com.smn.app.client.event.AppEvent;
import com.smn.app.client.network.NetworkController;
import com.smn.app.client.scene.AppSceneController;
import com.smn.app.client.scene.LoginSceneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

/**
 * Entry (and exit) point for the program
 */
public class Main extends Application {

    private NetworkController networkController;
    private Scene mainScene;
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        networkController = NetworkController.instance;

        // we need the stage to be able to change scenes later
        stage = primaryStage;

        loadLoginScene();

        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Music Social Network");
        primaryStage.show();

        primaryStage.addEventHandler(AppEvent.Login.Type, (event) -> {
            loadAppScene(event);
        });
        primaryStage.addEventHandler(AppEvent.Logout.Type, (event) -> {
            loadLoginScene();
        });

        networkController.connect();

        System.out.println("Application ready...");
    }

    /**
     * Sets the scene to be the login scene. This is also the scene for registering. It is the default scene.
     */
    private void loadLoginScene() {
        Parent rootNode;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginScene.fxml"));

            rootNode = loader.load();
        } catch (IOException e) {
            System.err.println("ERROR: Couldn't load login root node\n\t" + e.getMessage() + "\n");
            e.printStackTrace();
            Platform.exit();
            return;
        }

        mainScene = new Scene(rootNode);
        stage.setScene(mainScene);
    }

    /**
     * Sets the scene to be the main app scene, this is after a successful login.
     */
    private void loadAppScene(AppEvent.Login event) {
        Parent rootNode;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AppScene.fxml"));

            rootNode = loader.load();

            AppSceneController controller = loader.getController();
            controller.setUserDetails(event.username, event.password);
        } catch (IOException e) {
            System.err.println("ERROR: Couldn't load app root node\n\t" + e.getMessage() + "\n");
            e.printStackTrace();
            Platform.exit();
            return;
        }

        mainScene = new Scene(rootNode);
        stage.setScene(mainScene);
    }

    /**
     * Called at the end of the application, closes the network resources.
     */
    @Override
    public void stop() {
        System.out.println("Application closing...");

        networkController.close();
    }
}

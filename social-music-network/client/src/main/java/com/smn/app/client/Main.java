package com.smn.app.client;

import com.smn.app.client.event.AppEvent;
import com.smn.app.client.network.NetworkController;
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
            loadAppScene();
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
            System.out.println(getClass().getResource("/LoginScene.fxml"));
            rootNode = FXMLLoader.load(getClass().getResource("/LoginScene.fxml"));
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
    private void loadAppScene() {
        Parent rootNode;
        try {
            rootNode = FXMLLoader.load(getClass().getResource("/AppScene.fxml"));
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

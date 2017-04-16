package musicss.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import musicss.client.event.AppEvent;
import musicss.client.network.NetworkController;

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
        primaryStage.show();

        primaryStage.addEventHandler(AppEvent.Login.Type, (event) -> {
            loadAppScene();
        });
        primaryStage.addEventHandler(AppEvent.Logout.Type, (event) -> {
            loadLoginScene();
        });

        System.out.println("Application ready...");
    }

    /**
     * Sets the scene to be the login scene. This is also the scene for registering. It is the default scene.
     */
    private void loadLoginScene() {
        Parent rootNode;
        try {
            rootNode = FXMLLoader.load(getClass().getResource("LoginSceneRoot.fxml"));
        } catch (IOException e) {
            System.err.println("ERROR: Couldn't load login root node\n\t" + e.getMessage() + "\n");
            e.printStackTrace();
            Platform.exit();
            return;
        }

        mainScene = new Scene(rootNode, 500, 500);
        stage.setScene(mainScene);
    }

    /**
     * Sets the scene to be the main app scene, this is after a successful login.
     */
    private void loadAppScene() {
        Parent rootNode;
        try {
            rootNode = FXMLLoader.load(getClass().getResource("AppSceneRoot.fxml"));
        } catch (IOException e) {
            System.err.println("ERROR: Couldn't load app root node\n\t" + e.getMessage() + "\n");
            e.printStackTrace();
            Platform.exit();
            return;
        }

        mainScene = new Scene(rootNode, 500, 500);
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

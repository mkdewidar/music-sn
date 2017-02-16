package musicss.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.*;
import java.net.UnknownHostException;

/**
 * Program starts here.
 */
public class Main extends Application {

    private Stage mainStage;
    private Scene mainScene;

    private NetworkManager networkManager;

    @Override
    public void start(Stage primaryStage) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("ClientWindow.fxml"));
            mainScene = new Scene(root, 500, 500);
            primaryStage.setScene(mainScene);
        } catch (IOException e) {
            System.err.println("ERROR: Couldn't load ClientWindow.fxml\n\t" + e.getMessage());
            Platform.exit();
        }

        mainStage = primaryStage;
        mainStage.show();

        try {
            networkManager = new NetworkManager();

            System.out.println("You have been connected to the server.");

        } catch (UnknownHostException e) {
            System.err.println("ERROR: Couldn't find host address\n\t" + e.getMessage());
            return;
        } catch (IOException e) {
            System.err.println("ERROR: An IO error occurred during setup\n\t" + e.getMessage());
            return;
        }
    }

    @Override
    public void stop() {
        System.out.println("Disconnecting from server...");
        try {
            if (networkManager != null) {
                networkManager.Close();
            }
        } catch (IOException e) {
            System.err.println("ERROR: An IO error occurred while closing resources\n\t" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package musicss.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

import static musicss.client.NetworkManager.connectionManager;

/**
 * Program starts here.
 */
public class Main extends Application {

    private Stage mainStage;
    private Scene mainScene;

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
            connectionManager.Connect();
        } catch (IOException e) {
            System.err.println("ERROR: Failed to connect to server\n\t" + e.getMessage());
        }
    }

    @Override
    public void stop() {
        System.out.println("Disconnecting from server...");

        if (connectionManager != null) {
            connectionManager.Close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package musicss.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;

/**
 * Root controller of the program, mediates between the UI controls
 * and each other as well as between the UI controls and the network.
 */
public class Main extends Application {

    private BorderPane rootNode;
    // The reference to the network status banner
    private Parent netStatusBanner;

    private NetworkController networkController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        networkController = NetworkController.connectionController;

        // The node containing all login UI elements
        Parent loginNode;

        try {
            rootNode = FXMLLoader.load(getClass().getResource("RootNode.fxml"));
            netStatusBanner = FXMLLoader.load(getClass().getResource("NetStatusBanner.fxml"));
            loginNode = FXMLLoader.load(getClass().getResource("LoginContent.fxml"));

        } catch (IOException e) {
            System.err.println("ERROR: Couldn't load one or more of the fxml files\n\t" + e.getMessage());
            Platform.exit();
            return;
        }

        rootNode.setTop(netStatusBanner);
        rootNode.setCenter(loginNode);

        Scene mainScene = new Scene(rootNode, 500, 500);
        primaryStage.setScene(mainScene);
        primaryStage.show();

        setupEventFilters();
    }

    private void setupEventFilters() {
    }

    @Override
    public void stop() {
        System.out.println("Disconnecting from server...");

        if (networkController != null) {
            networkController.close();
        }
    }
}

package musicss.client;

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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        networkController = NetworkController.connectionController;

        // This root node will load the root parent node with a UI Controller that will manage everything
        // that has to do with the UI the user sees
        Parent rootNode;
        try {
            rootNode = FXMLLoader.load(getClass().getResource("RootNode.fxml"));
        } catch (IOException e) {
            System.err.println("ERROR: Couldn't load root node\n\t" + e.getMessage() + "\n");
            e.printStackTrace();
            Platform.exit();
            return;
        }

        Scene mainScene = new Scene(rootNode, 500, 500);

        primaryStage.setScene(mainScene);
        primaryStage.show();

        System.out.println("Application ready...");
    }

    @Override
    public void stop() {
        System.out.println("Application closing...");

        networkController.close();
    }
}

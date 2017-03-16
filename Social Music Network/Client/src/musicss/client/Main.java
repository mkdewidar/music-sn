package musicss.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;

import static musicss.client.ConnectionController.connectionController;

/**
 * Program starts here.
 */
public class Main extends Application {

    private Stage mainStage;
    private Scene mainScene;

    private Parent loginSubscene;
    private Parent connectionBanner;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("MasterScene.fxml"));
            loginSubscene = FXMLLoader.load(getClass().getResource("LoginSubscene.fxml"));
            connectionBanner = FXMLLoader.load(getClass().getResource("ConnectionBanner.fxml"));

        } catch (IOException e) {
            System.err.println("ERROR: Couldn't load one or more of the fxml files\n\t" + e.getMessage());
            Platform.exit();
            return;
        }

        root.setTop(connectionBanner);
        root.setCenter(loginSubscene);

        mainScene = new Scene(root, 500, 500);
        primaryStage.setScene(mainScene);

        mainStage = primaryStage;
        mainStage.show();
    }

    @Override
    public void stop() {
        System.out.println("Disconnecting from server...");

        if (connectionController != null) {
            connectionController.Close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

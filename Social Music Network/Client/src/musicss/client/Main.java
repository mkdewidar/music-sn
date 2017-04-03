package musicss.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import musicss.client.event.LoginEvent;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

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

        try {
            networkController.connect();
        } catch (IOException e) {
        }

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

        setupEventFilters();

        Scene mainScene = new Scene(rootNode, 500, 500);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void setupEventFilters() {
        // Event filters happen before handlers, this lets us do processing on the data before
        // letting the event get handled by another node
        rootNode.addEventFilter(LoginEvent.LoginEventType, new EventHandler<LoginEvent>() {
            @Override
            public void handle(LoginEvent event) {
                System.out.println("Sending login data over network");

                networkController.sendString("The username is: " + event.getUsername() +
                        ", and the pass is: " + event.getPassword());
            }
        });
    }

    @Override
    public void stop() {
        System.out.println("Disconnecting from server...");

        if (networkController != null) {
            networkController.close();
        }
    }
}

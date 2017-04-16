package musicss.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import musicss.client.control.LoginControl;
import musicss.client.control.StatusControl;
import musicss.client.event.AppEvent;
import musicss.protocol.message.Request;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls and manages the UI scene graph and all of it's controls.
 */
public class UIController implements Initializable {
    @FXML
    protected BorderPane rootNode;
    protected NetworkController networkController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        networkController = NetworkController.connectionController;

        rootNode.setCenter(new LoginControl());
        rootNode.setTop(new StatusControl());

        rootNode.addEventFilter(AppEvent.Login.Type, (event) -> {
        });
    }
}

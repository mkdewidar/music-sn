package musicss.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import musicss.client.control.LoginControl;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls and manages the UI scene graph and all of it's controls.
 */
public class UIController implements Initializable {
    @FXML
    protected BorderPane rootNode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootNode.setCenter(new LoginControl());
    }
}

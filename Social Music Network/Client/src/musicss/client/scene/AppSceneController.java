package musicss.client.scene;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import musicss.client.control.FriendsListControl;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for the main application scene.
 */
public class AppSceneController extends SceneController {
    @FXML
    protected BorderPane rootNode;

    protected FriendsListControl friendsList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        rootNode.setTop(statusBanner);
        friendsList = new FriendsListControl(this);
        rootNode.setLeft(friendsList);
    }
}

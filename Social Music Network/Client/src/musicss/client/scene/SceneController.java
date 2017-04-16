package musicss.client.scene;

import javafx.fxml.Initializable;
import musicss.client.control.StatusControl;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Base class for all scene controllers, consists of a status bar and the required interface for it.
 */
public class SceneController implements Initializable {
    protected StatusControl statusBanner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statusBanner = new StatusControl();
    }
}

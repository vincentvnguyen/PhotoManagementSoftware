package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LogWindowController implements Initializable {

    @FXML
    TextArea logField;

    /**
     * Passing data to LogWindowController. logData will be viewed on this screen
     *
     * @param logData the data being passed in and going to be displayed
     */
    void passData(List<String> logData) {
        StringBuilder text = new StringBuilder();
        for (String line : logData) {
            text.append(line);
            text.append(System.lineSeparator());
        }
        logField.setText(text.toString());
    }

    /**
     * Initializes LogWindowController
     *
     * @param url            location used to resolve relative paths for the root object
     * @param resourceBundle the resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

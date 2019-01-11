package pack;

import Controllers.StageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    /**
     * Setting up the stage
     *
     * @param stage the stage being showed
     */
    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../Views/MainView.fxml"));
            Scene scene = new Scene(root);

            stage.setTitle("The 100% A+ Award-Winning Photo Management Software");
            stage.getIcons().add(new Image("Images/trump-icon.png"));
            stage.setScene(scene);
            stage.setMinWidth(500);
            stage.setMinHeight(400);
            StageController.setStage(stage);
            stage.show();
        } catch (IOException ignored) {
            System.out.println(ignored);
        }
    }
}

package basketball;

import basketball.sceneManagement.SceneMangerFactory;
import basketball.sceneManagement.Scenes;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneMangerFactory manager = SceneMangerFactory.getInstance();
        manager.setPrimaryStage(primaryStage);
        manager.setScene(Scenes.Login);
        primaryStage.show();
    }
}

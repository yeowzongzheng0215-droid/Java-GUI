package CineStream;

import CineStream.Page.HomePage;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainApp extends Application {
    private static Stage primaryStage;
    private Scene scene;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("CineStream - Online Movie Booking");
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(960);
        primaryStage.setMinHeight(680);

        Pane home = new HomePage(this).getRoot();
        scene = new Scene(home, 1440, 900);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public void navigateTo(Pane root) {
        if (scene == null) {
            scene = new Scene(root, 1440, 900);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            primaryStage.setScene(scene);
        } else {
            scene.setRoot(root);
        }

        root.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(260), root);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package CineStream;

import CineStream.Page.HomePage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApp extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("CineStream - 电影订票系统");
        primaryStage.setResizable(false);
        navigateTo(new HomePage(this).getRoot());
        primaryStage.show();
    }

    public void navigateTo(Pane root) {
        Scene scene = new Scene(root, 950, 650);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
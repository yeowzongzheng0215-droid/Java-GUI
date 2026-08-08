package CineStream.Page;

import CineStream.MainApp;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CinemaPage {
    private final MainApp app;
    private final BorderPane root;

    public CinemaPage(MainApp app) {
        this.app = app;
        root = new BorderPane();
        root.setPadding(new Insets(25));

        Label title = new Label("ℹ️ About CineStream System");
        title.getStyleClass().add("title-label");

        Label content = new Label("""
                CineStream is a professional cinema ticket booking system developed using JavaFX.

                Core Features:
                • User registration and secure login
                • Browse available movies and screening schedules
                • Interactive seat selection with real-time status updates
                • Automatic fare calculation and payment processing
                • Digital ticket generation and viewing
                • Booking history management

                Version: 1.0.0
                Platform: JavaFX
                """);
        content.setWrapText(true);
        content.getStyleClass().add("normal-text");

        Button btnBack = new Button("Back to Home");
        btnBack.getStyleClass().add("button-full");
        btnBack.setOnAction(e -> app.navigateTo(new HomePage(app).getRoot()));

        VBox vbox = new VBox(15, title, content, btnBack);
        vbox.getStyleClass().add("card");
        vbox.setPadding(new Insets(20));

        root.setCenter(vbox);
    }

    public Pane getRoot() {
        return root;
    }
}
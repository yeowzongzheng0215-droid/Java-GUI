package CineStream.Page;

import CineStream.MainApp;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class AboutPage {
    private final MainApp app;
    private final BorderPane root;

    public AboutPage(MainApp app) {
        this.app = app;
        root = new BorderPane();
        root.setPadding(new Insets(20));

        Label title = new Label("ℹ️ About CineStream System");
        title.getStyleClass().add("title-label");

        Label content = new Label("""
                CineStream is a professional JavaFX-based movie ticket booking system.

                System Features:
                • Secure user registration and login (Gmail only)
                • Browse available movies and showtimes
                • Interactive seat selection with real-time status update
                • Prevent duplicate bookings
                • Automatic price calculation
                • Digital ticket generation
                • View and manage booking history
                • Modular and maintainable architecture

                Designed for clarity, efficiency, and user-friendly operation.
                """);
        content.setWrapText(true);
        content.getStyleClass().add("normal-text");

        Button btnBack = new Button("Back to Home");
        btnBack.getStyleClass().add("button-full");
        btnBack.setOnAction(e -> app.navigateTo(new HomePage(app).getRoot()));

        VBox vbox = new VBox(15, title, content, btnBack);
        vbox.getStyleClass().add("card");
        vbox.setPadding(new Insets(25));

        root.setCenter(vbox);
    }

    public Pane getRoot() {
        return root;
    }
}
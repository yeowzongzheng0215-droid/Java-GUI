package CineStream.Page;

import CineStream.MainApp;
import CineStream.UIComponents;
import CineStream.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class CinemaPage {
    private final MainApp app;
    private final BorderPane root;

    public CinemaPage(MainApp app) {
        this.app = app;
        root = UIComponents.createPage(app, "cinemas");

        VBox intro = UIComponents.pageIntro(
                "Our locations",
                "Big screens, close to home.",
                "Explore CineStream cinema concepts inspired by Malaysia's favourite movie destinations."
        );

        FlowPane locations = new FlowPane(20, 20);
        locations.setAlignment(Pos.TOP_CENTER);
        locations.getChildren().addAll(
                cinemaCard("KUALA LUMPUR", "CineStream Pavilion", "12 halls • IMAX • Dolby Atmos", "168 Jalan Bukit Bintang, Kuala Lumpur"),
                cinemaCard("PETALING JAYA", "CineStream 1 Utama", "10 halls • 4K Laser • D-BOX", "Bandar Utama, Petaling Jaya"),
                cinemaCard("JOHOR BAHRU", "CineStream Mid Valley Southkey", "8 halls • IMAX • Premium Suites", "Persiaran Southkey 1, Johor Bahru")
        );

        Label noteTitle = new Label("A better cinema experience");
        noteTitle.getStyleClass().add("section-heading");
        Label noteBody = new Label(
                "Every location offers accessible seating, cashless checkout, digital ticket scanning and a curated snack counter. "
                        + "Cinema names and addresses in this student project are demonstration content."
        );
        noteBody.setWrapText(true);
        noteBody.getStyleClass().add("about-copy");
        VBox note = new VBox(12, noteTitle, noteBody);
        note.setPadding(new Insets(28));
        note.getStyleClass().add("glass-card");

        VBox content = UIComponents.pageContent(intro, locations, note);
        root.setCenter(UIComponents.scrollable(content));
    }

    private VBox cinemaCard(String city, String name, String formats, String address) {
        Label cityLabel = new Label(city);
        cityLabel.getStyleClass().add("eyebrow");
        Label nameLabel = new Label(name);
        nameLabel.setWrapText(true);
        nameLabel.getStyleClass().add("cinema-name");
        Label formatsLabel = new Label(formats);
        formatsLabel.setWrapText(true);
        formatsLabel.getStyleClass().add("cinema-formats");
        Label addressLabel = new Label(address);
        addressLabel.setWrapText(true);
        addressLabel.getStyleClass().add("muted-text");

        Button showtimes = UIComponents.primaryButton("View showtimes");
        showtimes.setMaxWidth(Double.MAX_VALUE);
        showtimes.setOnAction(e -> openShowtimes());
        Button directions = UIComponents.ghostButton("Location details");
        directions.setMaxWidth(Double.MAX_VALUE);
        directions.setOnAction(e -> UIComponents.showAlert(Alert.AlertType.INFORMATION,
                name, address + "\n\n" + formats));

        VBox card = new VBox(14, cityLabel, nameLabel, formatsLabel, addressLabel, showtimes, directions);
        card.setPadding(new Insets(26));
        card.setPrefWidth(340);
        card.getStyleClass().addAll("glass-card", "cinema-card");
        UIComponents.addHoverScale(card, 1.02);
        return card;
    }

    private void openShowtimes() {
        if (UserSession.getInstance().isLoggedIn()) {
            app.navigateTo(new MoviePage(app).getRoot());
        } else {
            UIComponents.showAlert(Alert.AlertType.INFORMATION, "Sign in to book",
                    "Please sign in before selecting your movie and seats.");
            app.navigateTo(new LoginPage(app).getRoot());
        }
    }

    public Pane getRoot() {
        return root;
    }
}

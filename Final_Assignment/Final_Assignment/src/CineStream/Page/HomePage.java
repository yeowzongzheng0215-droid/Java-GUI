package CineStream.Page;

import CineStream.MainApp;
import CineStream.Model.MovieCatalog;
import CineStream.Model.MovieData;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class HomePage {
    private final MainApp app;
    private final BorderPane root;

    public HomePage(MainApp app) {
        this.app = app;
        root = UIComponents.createPage(app, "home");

        VBox content = UIComponents.pageContent(
                createHero(),
                createNowShowing(),
                createBenefits(),
                createFooter()
        );
        root.setCenter(UIComponents.scrollable(content));
    }

    private StackPane createHero() {
        StackPane hero = UIComponents.mediaFrame(
                "/CineStream/assets/banner/cinema-hero.png",
                "HERO BANNER • REPLACE cinema-hero.png",
                1400,
                460
        );
        hero.getStyleClass().add("hero-banner");

        Label badge = new Label("NOW SHOWING • BOOK IN MINUTES");
        badge.getStyleClass().add("hero-badge");

        Label title = new Label("Your next great\nmovie night starts here.");
        title.setWrapText(true);
        title.getStyleClass().add("hero-title");

        Label subtitle = new Label(
                "Discover the latest blockbusters, choose your favourite seats and get your e-ticket instantly."
        );
        subtitle.setWrapText(true);
        subtitle.getStyleClass().add("hero-subtitle");

        Button explore = UIComponents.primaryButton("Explore movies  →");
        explore.setOnAction(e -> startBooking());
        Button cinemas = UIComponents.secondaryButton("View cinemas");
        cinemas.setOnAction(e -> app.navigateTo(new CinemaPage(app).getRoot()));
        HBox actions = new HBox(12, explore, cinemas);

        Label reassurance = new Label("✓ Secure checkout     ✓ Instant confirmation     ✓ Flexible seat selection");
        reassurance.getStyleClass().add("hero-reassurance");

        VBox copy = new VBox(16, badge, title, subtitle, actions, reassurance);
        copy.setMaxWidth(680);
        copy.setAlignment(Pos.CENTER_LEFT);
        StackPane.setAlignment(copy, Pos.CENTER_LEFT);
        StackPane.setMargin(copy, new Insets(44, 48, 44, 64));
        hero.getChildren().add(copy);
        return hero;
    }

    private VBox createNowShowing() {
        Label eyebrow = new Label("ON THE BIG SCREEN");
        eyebrow.getStyleClass().add("eyebrow");
        Label heading = new Label("Now showing");
        heading.getStyleClass().add("section-heading");
        Label subheading = new Label("Hand-picked stories, premium halls and showtimes throughout the day.");
        subheading.getStyleClass().add("muted-text");

        VBox headingGroup = new VBox(5, eyebrow, heading, subheading);
        Button allMovies = UIComponents.ghostButton("See all showtimes  →");
        allMovies.setOnAction(e -> startBooking());
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox sectionHeader = new HBox(15, headingGroup, spacer, allMovies);
        sectionHeader.setAlignment(Pos.BOTTOM_LEFT);

        FlowPane movieGrid = new FlowPane(22, 22);
        movieGrid.getStyleClass().add("movie-grid");
        movieGrid.setAlignment(Pos.TOP_CENTER);
        for (MovieData movie : MovieCatalog.getInstance().getMovies()) {
            movieGrid.getChildren().add(createMovieCard(movie));
        }

        return new VBox(22, sectionHeader, movieGrid);
    }

    private VBox createMovieCard(MovieData movie) {
        StackPane poster = UIComponents.posterFor(movie, 300, 400);
        poster.getStyleClass().add("poster-image");

        Label rating = new Label("13+");
        rating.getStyleClass().add("rating-chip");
        StackPane.setAlignment(rating, Pos.TOP_LEFT);
        StackPane.setMargin(rating, new Insets(14));
        poster.getChildren().add(rating);

        Label title = new Label(movie.getTitle());
        title.setWrapText(true);
        title.getStyleClass().add("movie-title");
        Label meta = new Label(movie.getDescription());
        meta.setWrapText(true);
        meta.getStyleClass().add("movie-meta");
        Label duration = new Label(movie.getDuration() + " min   •   EN");
        duration.getStyleClass().add("movie-duration");

        Button book = UIComponents.primaryButton("Book tickets");
        book.setMaxWidth(Double.MAX_VALUE);
        book.setOnAction(e -> openMovie(movie));

        VBox card = new VBox(12, poster, title, meta, duration, book);
        card.getStyleClass().add("movie-display-card");
        card.setPrefWidth(300);
        card.setMaxWidth(320);
        UIComponents.addHoverScale(card, 1.018);
        return card;
    }

    private FlowPane createBenefits() {
        FlowPane benefits = new FlowPane(18, 18);
        benefits.setAlignment(Pos.CENTER);
        benefits.getChildren().addAll(
                UIComponents.featureCard("01", "Pick your movie", "Browse current releases and compare formats, dates and showtimes."),
                UIComponents.featureCard("02", "Choose your seats", "Our interactive seat map updates your selection and total instantly."),
                UIComponents.featureCard("03", "Enjoy the show", "Pay securely, receive your e-ticket and head straight to the hall.")
        );
        return benefits;
    }

    private HBox createFooter() {
        Label brand = new Label("CINESTREAM");
        brand.getStyleClass().add("footer-brand");
        Label note = new Label("A modern JavaFX online movie booking experience.");
        note.getStyleClass().add("muted-text");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label copyright = new Label("© 2026 CineStream");
        copyright.getStyleClass().add("muted-text");
        HBox footer = new HBox(16, brand, note, spacer, copyright);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.getStyleClass().add("app-footer");
        return footer;
    }

    private void startBooking() {
        if (UserSession.getInstance().isLoggedIn()) {
            app.navigateTo(new MoviePage(app).getRoot());
        } else {
            UIComponents.showAlert(Alert.AlertType.INFORMATION, "Sign in to book",
                    "Create an account or sign in first. Your movie selection will only take a moment.");
            app.navigateTo(new LoginPage(app).getRoot());
        }
    }

    private void openMovie(MovieData movie) {
        if (UserSession.getInstance().isLoggedIn()) {
            app.navigateTo(new MoviePage(app, movie).getRoot());
        } else {
            UIComponents.showAlert(Alert.AlertType.INFORMATION, "Sign in to book",
                    "Please sign in before choosing a showtime and seats.");
            app.navigateTo(new LoginPage(app).getRoot());
        }
    }

    public Pane getRoot() {
        return root;
    }
}

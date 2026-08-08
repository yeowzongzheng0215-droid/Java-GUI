package CineStream.Page;

import CineStream.BookingUtils;
import CineStream.MainApp;
import CineStream.Model.MovieCatalog;
import CineStream.Model.MovieData;
import CineStream.Model.Showtime;
import CineStream.UIComponents;
import CineStream.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MoviePage {
    private final MainApp app;
    private final BorderPane root;
    private final List<MovieData> movies = MovieCatalog.getInstance().getMovies();
    private final ToggleGroup movieGroup = new ToggleGroup();
    private final ToggleGroup showtimeGroup = new ToggleGroup();
    private final FlowPane showtimeOptions = new FlowPane(12, 12);
    private final Label showtimeHint = new Label();
    private final Label selectionText = new Label("Choose a movie and showtime to continue");
    private final Button proceedButton = UIComponents.primaryButton("Choose seats  →");
    private MovieData selectedMovie;
    private Showtime selectedShowtime;

    public MoviePage(MainApp app) {
        this(app, UserSession.getInstance().getSelectedMovie());
    }

    public MoviePage(MainApp app, MovieData preferredMovie) {
        this.app = app;
        root = UIComponents.createPage(app, "movies");

        VBox intro = UIComponents.pageIntro(
                "Step 1 of 5",
                "What would you like to watch?",
                "Select a movie, then choose the screening time that works best for you."
        );

        FlowPane movieOptions = new FlowPane(18, 18);
        movieOptions.setAlignment(Pos.TOP_CENTER);
        movieOptions.getStyleClass().add("movie-choice-grid");
        ToggleButton preferredButton = null;
        for (MovieData movie : movies) {
            ToggleButton option = createMovieOption(movie);
            movieOptions.getChildren().add(option);
            if (movie == preferredMovie) {
                preferredButton = option;
            }
        }

        showtimeHint.setText("Select a movie above to view available sessions.");
        showtimeHint.getStyleClass().add("muted-text");
        showtimeOptions.setAlignment(Pos.CENTER_LEFT);

        Label timeTitle = new Label("Available showtimes");
        timeTitle.getStyleClass().add("section-heading-small");
        Label cinema = new Label("CineStream Pavilion • Kuala Lumpur");
        cinema.getStyleClass().add("location-label");
        VBox timeHeading = new VBox(5, timeTitle, cinema);

        VBox showtimeCard = new VBox(18, timeHeading, showtimeHint, showtimeOptions);
        showtimeCard.getStyleClass().addAll("glass-card", "showtime-card");
        showtimeCard.setPadding(new Insets(24));

        selectionText.getStyleClass().add("selection-summary-text");
        proceedButton.setDisable(true);
        proceedButton.setOnAction(e -> proceedToSeats());
        Button back = UIComponents.secondaryButton("Back to home");
        back.setOnAction(e -> app.navigateTo(new HomePage(app).getRoot()));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox actionBar = new HBox(14, selectionText, spacer, back, proceedButton);
        actionBar.setAlignment(Pos.CENTER_LEFT);
        actionBar.getStyleClass().add("sticky-action-card");

        VBox content = UIComponents.pageContent(
                UIComponents.bookingSteps(0),
                intro,
                movieOptions,
                showtimeCard,
                actionBar
        );
        root.setCenter(UIComponents.scrollable(content));

        if (preferredButton == null && !movies.isEmpty()) {
            preferredButton = (ToggleButton) movieOptions.getChildren().get(0);
        }
        if (preferredButton != null) {
            preferredButton.setSelected(true);
            selectMovie((MovieData) preferredButton.getUserData());
        }
    }

    private ToggleButton createMovieOption(MovieData movie) {
        StackPane poster = UIComponents.posterFor(movie, 220, 292);
        Label title = new Label(movie.getTitle());
        title.setWrapText(true);
        title.getStyleClass().add("movie-choice-title");
        Label genre = new Label(movie.getDescription());
        genre.setWrapText(true);
        genre.getStyleClass().add("movie-meta");
        Label duration = new Label(movie.getDuration() + " min");
        duration.getStyleClass().add("duration-chip");

        VBox graphic = new VBox(10, poster, title, genre, duration);
        graphic.setAlignment(Pos.TOP_LEFT);
        graphic.setPrefWidth(220);

        ToggleButton option = new ToggleButton();
        option.setGraphic(graphic);
        option.setUserData(movie);
        option.setToggleGroup(movieGroup);
        option.getStyleClass().add("movie-choice-button");
        option.setOnAction(e -> {
            if (!option.isSelected()) {
                option.setSelected(true);
            }
            selectMovie(movie);
        });
        UIComponents.addHoverScale(option, 1.02);
        return option;
    }

    private void selectMovie(MovieData movie) {
        selectedMovie = movie;
        selectedShowtime = null;
        showtimeGroup.selectToggle(null);
        showtimeOptions.getChildren().clear();
        List<Showtime> times = MovieCatalog.getInstance().getShowtimesFor(movie);
        DateTimeFormatter dayFormat = DateTimeFormatter.ofPattern("EEE, d MMM");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h:mm a");

        for (Showtime showtime : times) {
            Label day = new Label(showtime.getTime().format(dayFormat));
            day.getStyleClass().add("showtime-day");
            Label time = new Label(showtime.getTime().format(timeFormat));
            time.getStyleClass().add("showtime-time");
            Label hall = new Label(showtime.getHall().getName());
            hall.getStyleClass().add("showtime-hall");
            VBox graphic = new VBox(4, day, time, hall);
            graphic.setAlignment(Pos.CENTER_LEFT);

            ToggleButton button = new ToggleButton();
            button.setGraphic(graphic);
            button.setToggleGroup(showtimeGroup);
            button.setUserData(showtime);
            button.getStyleClass().add("showtime-button");
            button.setOnAction(e -> {
                if (!button.isSelected()) {
                    button.setSelected(true);
                }
                selectedShowtime = showtime;
                selectionText.setText(movie.getTitle() + "  •  "
                        + showtime.getTime().format(DateTimeFormatter.ofPattern("d MMM, h:mm a")));
                proceedButton.setDisable(false);
            });
            UIComponents.addHoverScale(button, 1.035);
            showtimeOptions.getChildren().add(button);
        }

        showtimeHint.setText(times.size() + " sessions available • RM 18.00 per seat");
        selectionText.setText(movie.getTitle() + " • Select a showtime");
        proceedButton.setDisable(true);
    }

    private void proceedToSeats() {
        if (selectedMovie == null || selectedShowtime == null) {
            UIComponents.showAlert(Alert.AlertType.WARNING, "Showtime required",
                    "Please select one of the available showtimes before continuing.");
            return;
        }

        UserSession session = UserSession.getInstance();
        if (session.getSelectedShowtime() != selectedShowtime) {
            BookingUtils.releaseSelection(session.getSelectedSeats());
            session.setSelectedSeats(List.of());
        }
        session.setSelectedMovie(selectedMovie);
        session.setSelectedShowtime(selectedShowtime);
        app.navigateTo(new SeatPage(app).getRoot());
    }

    public Pane getRoot() {
        return root;
    }
}

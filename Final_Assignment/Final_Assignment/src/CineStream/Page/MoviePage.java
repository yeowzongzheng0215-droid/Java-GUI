package CineStream.Page;

import CineStream.MainApp;
import CineStream.Model.MovieData;
import CineStream.Model.ScreeningHall;
import CineStream.Model.Showtime;
import CineStream.UserSession;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MoviePage {
    private final MainApp app;
    private final BorderPane root;
    private final List<MovieData> movies = new ArrayList<>();
    private final List<Showtime> showtimes = new ArrayList<>();

    public MoviePage(MainApp app) {
        this.app = app;
        root = new BorderPane();
        root.setPadding(new Insets(20));

        initSampleData();

        Label title = new Label("🎞️ Movie List & Showtime Selection");
        title.getStyleClass().add("title-label");

        Label lblMovie = new Label("Select Movie:");
        lblMovie.getStyleClass().add("section-title");

        ListView<MovieData> movieList = new ListView<>();
        movieList.getItems().addAll(movies);
        movieList.setPrefHeight(200);

        Label lblTime = new Label("Select Showtime:");
        lblTime.getStyleClass().add("section-title");

        ListView<Showtime> timeList = new ListView<>();
        timeList.setPrefHeight(250);

        movieList.getSelectionModel().selectedItemProperty().addListener((obs, old, now) -> {
            if (now != null) {
                UserSession.getInstance().setSelectedMovie(now);
                timeList.getItems().setAll(showtimes.stream()
                        .filter(s -> s.getMovie().equals(now))
                        .toList());
            }
        });

        Button btnNext = new Button("Proceed to Seat Selection");
        btnNext.getStyleClass().add("button-full");
        btnNext.setOnAction(e -> {
            Showtime selected = timeList.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Please select a showtime first.").show();
                return;
            }
            UserSession.getInstance().setSelectedShowtime(selected);
            app.navigateTo(new SeatPage(app).getRoot());
        });

        Button btnBack = new Button("Back to Home");
        btnBack.getStyleClass().add("button-full");
        btnBack.setOnAction(e -> app.navigateTo(new HomePage(app).getRoot()));

        VBox center = new VBox(10,
                title, lblMovie, movieList, lblTime, timeList, btnNext, btnBack
        );
        center.getStyleClass().add("card");
        center.setPadding(new Insets(20));

        root.setCenter(center);
    }

    private void initSampleData() {
        movies.add(new MovieData("The Wandering Earth 3", "", "Sci-Fi / Action", 145));
        movies.add(new MovieData("Zootopia 2", "", "Animation / Adventure", 110));
        movies.add(new MovieData("Avengers: Final Chapter", "", "Superhero / Action", 160));

        ScreeningHall h1 = new ScreeningHall("Hall 1 (IMAX)", 8, 10);
        ScreeningHall h2 = new ScreeningHall("Hall 2 (4K)", 7, 9);
        ScreeningHall h3 = new ScreeningHall("Hall 3 (Standard)", 6, 8);

        showtimes.add(new Showtime(movies.get(0), LocalDateTime.now().withHour(10).withMinute(0), h1));
        showtimes.add(new Showtime(movies.get(0), LocalDateTime.now().withHour(15).withMinute(30), h1));
        showtimes.add(new Showtime(movies.get(1), LocalDateTime.now().withHour(12).withMinute(0), h2));
        showtimes.add(new Showtime(movies.get(1), LocalDateTime.now().withHour(18).withMinute(0), h2));
        showtimes.add(new Showtime(movies.get(2), LocalDateTime.now().withHour(14).withMinute(0), h3));
        showtimes.add(new Showtime(movies.get(2), LocalDateTime.now().withHour(20).withMinute(0), h3));
    }

    public Pane getRoot() {
        return root;
    }
}
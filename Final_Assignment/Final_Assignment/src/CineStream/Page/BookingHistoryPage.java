package CineStream.Page;

import CineStream.MainApp;
import CineStream.Model.ReceiptGenerator;
import CineStream.Model.Reservation;
import CineStream.Model.Seat;
import CineStream.Model.Ticket;
import CineStream.UIComponents;
import CineStream.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookingHistoryPage {
    public static final List<Reservation> ALL_BOOKINGS = new ArrayList<>();

    private final MainApp app;
    private final BorderPane root;

    public BookingHistoryPage(MainApp app) {
        this.app = app;
        root = UIComponents.createPage(app, "");

        VBox intro = UIComponents.pageIntro(
                "Your account",
                "My bookings",
                "Find every booking made during this application session and reopen its ticket details."
        );

        ListView<Reservation> list = new ListView<>();
        list.getStyleClass().add("booking-list");
        list.setPrefHeight(430);
        String currentUser = UserSession.getInstance().getUsername();
        ALL_BOOKINGS.stream()
                .filter(reservation -> reservation.getUsername().equals(currentUser))
                .forEach(list.getItems()::add);

        Label empty = new Label("No bookings yet\nYour next movie night will appear here.");
        empty.setAlignment(Pos.CENTER);
        empty.getStyleClass().add("empty-state");
        list.setPlaceholder(empty);
        list.setCellFactory(lv -> new BookingCell());

        Button viewTicket = UIComponents.primaryButton("View e-ticket");
        viewTicket.setOnAction(e -> viewTicket(list.getSelectionModel().getSelectedItem()));
        Button bookMovie = UIComponents.secondaryButton("Book another movie");
        bookMovie.setOnAction(e -> app.navigateTo(new MoviePage(app).getRoot()));
        Button home = UIComponents.ghostButton("Back to home");
        home.setOnAction(e -> app.navigateTo(new HomePage(app).getRoot()));
        HBox actions = new HBox(12, viewTicket, bookMovie, home);
        actions.setAlignment(Pos.CENTER_RIGHT);

        VBox historyCard = new VBox(18, list, actions);
        historyCard.setPadding(new Insets(24));
        historyCard.getStyleClass().add("glass-card");

        VBox content = UIComponents.pageContent(intro, historyCard);
        root.setCenter(UIComponents.scrollable(content));
    }

    private void viewTicket(Reservation selected) {
        if (selected == null) {
            UIComponents.showAlert(Alert.AlertType.WARNING, "Select a booking",
                    "Choose a booking from the list before viewing its e-ticket.");
            return;
        }

        Ticket ticket = new Ticket(
                "CS-HIS-" + Math.abs(selected.hashCode()),
                selected.getUsername(),
                selected.getMovie(),
                selected.getShowtime(),
                selected.getSeats(),
                ReceiptGenerator.PRICE_PER_SEAT
        );
        UIComponents.showAlert(Alert.AlertType.INFORMATION, "E-ticket details", ticket.getTicketText());
    }

    private static class BookingCell extends ListCell<Reservation> {
        @Override
        protected void updateItem(Reservation reservation, boolean empty) {
            super.updateItem(reservation, empty);
            if (empty || reservation == null) {
                setText(null);
                setGraphic(null);
                return;
            }

            Label status = new Label("CONFIRMED");
            status.getStyleClass().add("success-chip");
            Label movie = new Label(reservation.getMovie().getTitle());
            movie.getStyleClass().add("history-movie-title");
            Label date = new Label(reservation.getShowtime().getTime()
                    .format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy • h:mm a")));
            date.getStyleClass().add("muted-text");
            Label hall = new Label(reservation.getShowtime().getHall().getName());
            hall.getStyleClass().add("muted-text");
            VBox movieDetails = new VBox(6, status, movie, date, hall);

            String seatsText = reservation.getSeats().stream()
                    .map(Seat::getSeatNumber)
                    .collect(Collectors.joining(", "));
            Label seatsCaption = new Label("SEATS");
            seatsCaption.getStyleClass().add("detail-label");
            Label seats = new Label(seatsText);
            seats.getStyleClass().add("history-seat-value");
            VBox seatDetails = new VBox(5, seatsCaption, seats);
            seatDetails.setAlignment(Pos.CENTER_RIGHT);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            HBox row = new HBox(18, movieDetails, spacer, seatDetails);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(16));
            row.getStyleClass().add("history-row");
            setText(null);
            setGraphic(row);
        }
    }

    public Pane getRoot() {
        return root;
    }
}

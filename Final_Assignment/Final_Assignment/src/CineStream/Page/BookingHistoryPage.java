package CineStream.Page;

import CineStream.MainApp;
import CineStream.Model.ReceiptGenerator;
import CineStream.Model.Reservation;
import CineStream.Model.Ticket;
import CineStream.UserSession;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

public class BookingHistoryPage {
    private final MainApp app;
    private final BorderPane root;
    public static final List<Reservation> ALL_BOOKINGS = new ArrayList<>();

    public BookingHistoryPage(MainApp app) {
        this.app = app;
        root = new BorderPane();
        root.setPadding(new Insets(20));

        Label title = new Label("📋 Booking History");
        title.getStyleClass().add("title-label");

        ListView<Reservation> list = new ListView<>();
        String currentUser = UserSession.getInstance().getUsername();

        ALL_BOOKINGS.stream()
                .filter(r -> r.getUsername().equals(currentUser))
                .forEach(reservation -> list.getItems().add(reservation));

        list.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Reservation res, boolean empty) {
                super.updateItem(res, empty);
                if (empty || res == null) {
                    setText(null);
                } else {
                    setText(String.format(
                        "Date: %s | Movie: %s | Hall: %s | Seats: %s",
                        res.getBookTime().toLocalDate(),
                        res.getMovie().getTitle(),
                        res.getShowtime().getHall().getName(),
                        res.getSeats().stream().map(seat -> seat.getSeatNumber()).toList()
                    ));
                }
            }
        });

        Button btnViewTicket = new Button("View E-Ticket");
        btnViewTicket.getStyleClass().add("button-full");
        btnViewTicket.setOnAction(e -> {
            Reservation selected = list.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Please select a booking record first.").show();
                return;
            }
            Ticket t = new Ticket(
                "CS-HIS-" + selected.hashCode(),
                selected.getUsername(),
                selected.getMovie(),
                selected.getShowtime(),
                selected.getSeats(),
                ReceiptGenerator.PRICE_PER_SEAT
            );
            new Alert(Alert.AlertType.INFORMATION, t.getTicketText()).show();
        });

        Button btnBack = new Button("Back to Home");
        btnBack.getStyleClass().add("button-full");
        btnBack.setOnAction(e -> app.navigateTo(new HomePage(app).getRoot()));

        VBox vbox = new VBox(10, title, list, btnViewTicket, btnBack);
        vbox.getStyleClass().add("card");
        vbox.setPadding(new Insets(20));

        root.setCenter(vbox);
    }

    public Pane getRoot() {
        return root;
    }
}
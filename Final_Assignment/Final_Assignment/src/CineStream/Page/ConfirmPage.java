package CineStream.Page;

import CineStream.MainApp;
import CineStream.UserSession;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.time.format.DateTimeFormatter;

public class ConfirmPage {
    private final MainApp app;
    private final BorderPane root;

    public ConfirmPage(MainApp app) {
        this.app = app;
        root = new BorderPane();
        root.setPadding(new Insets(20));

        Label title = new Label("📝 Confirm Booking Details");
        title.getStyleClass().add("title-label");

        UserSession session = UserSession.getInstance();
        String info = String.format("""
                Customer Email: %s
                Movie Title: %s
                Screening Hall: %s
                Showtime: %s
                Selected Seats: %s
                """,
                session.getUsername(),
                session.getSelectedMovie().getTitle(),
                session.getSelectedShowtime().getHall().getName(),
                session.getSelectedShowtime().getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                session.getSelectedSeats().stream().map(seat -> seat.getSeatNumber()).toList()
        );

        Label lblInfo = new Label(info);
        lblInfo.getStyleClass().add("normal-text");

        Button btnPay = new Button("Proceed to Payment");
        btnPay.getStyleClass().add("button-full");
        btnPay.setOnAction(e -> app.navigateTo(new PaymentPage(app).getRoot()));

        Button btnBack = new Button("Modify Seats");
        btnBack.getStyleClass().add("button-full");
        btnBack.setOnAction(e -> app.navigateTo(new SeatPage(app).getRoot()));

        VBox vbox = new VBox(12, title, lblInfo, btnPay, btnBack);
        vbox.getStyleClass().add("card");
        vbox.setPadding(new Insets(20));

        root.setCenter(vbox);
    }

    public Pane getRoot() {
        return root;
    }
}
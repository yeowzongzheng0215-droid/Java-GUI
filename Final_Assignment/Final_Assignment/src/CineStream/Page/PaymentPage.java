package CineStream.Page;

import CineStream.MainApp;
import CineStream.Model.Reservation;
import CineStream.UserSession;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class PaymentPage {
    private final MainApp app;
    private final BorderPane root;

    public PaymentPage(MainApp app) {
        this.app = app;
        root = new BorderPane();
        root.setPadding(new Insets(20));

        UserSession session = UserSession.getInstance();
        double price = session.getSelectedSeats().size() * 18.0;

        Label title = new Label("💳 Payment Processing");
        title.getStyleClass().add("title-label");

        Label lblPrice = new Label(String.format("Total Amount: RM %.2f", price));
        lblPrice.getStyleClass().add("price-text");

        Button btnPay = new Button("Confirm Payment");
        btnPay.getStyleClass().add("button-full");
        btnPay.setOnAction(e -> {
            BookingHistoryPage.ALL_BOOKINGS.add(new Reservation(
                    session.getUsername(),
                    session.getSelectedMovie(),
                    session.getSelectedShowtime(),
                    session.getSelectedSeats()
            ));
            app.navigateTo(new TicketPage(app).getRoot());
        });

        Button btnBack = new Button("Back to Edit");
        btnBack.getStyleClass().add("button-full");
        btnBack.setOnAction(e -> app.navigateTo(new ConfirmPage(app).getRoot()));

        VBox vbox = new VBox(12, title, lblPrice, btnPay, btnBack);
        vbox.getStyleClass().add("card");
        vbox.setPadding(new Insets(20));

        root.setCenter(vbox);
    }

    public Pane getRoot() {
        return root;
    }
}
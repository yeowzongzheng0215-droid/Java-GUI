package CineStream.Page;

import CineStream.MainApp;
import CineStream.Model.ReceiptGenerator;
import CineStream.UserSession;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class PricePage {
    private final MainApp app;
    private final BorderPane root;

    public PricePage(MainApp app) {
        this.app = app;
        root = new BorderPane();
        root.setPadding(new Insets(20));

        Label title = new Label("💰 Booking Summary");
        title.getStyleClass().add("title-label");

        UserSession session = UserSession.getInstance();
        int seats = session.getSelectedSeats().size();
        double unitPrice = ReceiptGenerator.PRICE_PER_SEAT;
        double total = seats * unitPrice;

        Label info = new Label(String.format("""
Unit Price      : RM %.2f per seat
Number of Seats : %d
Total Amount    : RM %.2f
""", unitPrice, seats, total));
        info.getStyleClass().add("normal-text");

        Button btnNext = new Button("Proceed to Confirmation");
        btnNext.getStyleClass().add("button-full");
        btnNext.setOnAction(e -> app.navigateTo(new ConfirmPage(app).getRoot()));

        Button btnBack = new Button("Back to Seat Selection");
        btnBack.getStyleClass().add("button-full");
        btnBack.setOnAction(e -> app.navigateTo(new SeatPage(app).getRoot()));

        VBox vbox = new VBox(12, title, info, btnNext, btnBack);
        vbox.getStyleClass().add("card");
        vbox.setPadding(new Insets(20));

        root.setCenter(vbox);
    }

    public Pane getRoot() {
        return root;
    }
}
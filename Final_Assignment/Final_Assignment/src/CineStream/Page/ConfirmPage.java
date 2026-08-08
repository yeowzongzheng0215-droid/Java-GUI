package CineStream.Page;

import CineStream.MainApp;
import CineStream.Model.ReceiptGenerator;
import CineStream.Model.Seat;
import CineStream.UIComponents;
import CineStream.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class ConfirmPage {
    private final MainApp app;
    private final BorderPane root;

    public ConfirmPage(MainApp app) {
        this.app = app;
        root = UIComponents.createPage(app, "movies");
        UserSession session = UserSession.getInstance();

        String seats = session.getSelectedSeats().stream()
                .map(Seat::getSeatNumber)
                .collect(Collectors.joining(", "));
        double total = session.getSelectedSeats().size() * ReceiptGenerator.PRICE_PER_SEAT;

        VBox intro = UIComponents.pageIntro(
                "Final check",
                "Everything look right?",
                "Confirm the details below. You can still return to the seat map before payment."
        );

        VBox details = new VBox(0);
        details.getStyleClass().addAll("glass-card", "confirmation-card");
        details.setPadding(new Insets(8, 28, 8, 28));
        details.getChildren().addAll(
                confirmationRow("Movie", session.getSelectedMovie().getTitle()),
                confirmationRow("Date", session.getSelectedShowtime().getTime()
                        .format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy"))),
                confirmationRow("Time", session.getSelectedShowtime().getTime()
                        .format(DateTimeFormatter.ofPattern("h:mm a"))),
                confirmationRow("Cinema", "CineStream Pavilion"),
                confirmationRow("Hall", session.getSelectedShowtime().getHall().getName()),
                confirmationRow("Seats", seats),
                confirmationRow("Booked for", session.getUsername())
        );

        Label totalLabel = new Label(String.format("RM %.2f", total));
        totalLabel.getStyleClass().add("checkout-total-price");
        Label totalCaption = new Label("TOTAL PAYABLE");
        totalCaption.getStyleClass().add("detail-label");
        VBox totalGroup = new VBox(5, totalCaption, totalLabel);

        Button pay = UIComponents.primaryButton("Proceed to payment  →");
        pay.setOnAction(e -> app.navigateTo(new PaymentPage(app).getRoot()));
        Button edit = UIComponents.secondaryButton("Modify seats");
        edit.setOnAction(e -> app.navigateTo(new SeatPage(app).getRoot()));
        HBox buttons = new HBox(12, edit, pay);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox checkoutBar = new HBox(18, totalGroup, spacer, buttons);
        checkoutBar.setAlignment(Pos.CENTER_LEFT);
        checkoutBar.getStyleClass().add("checkout-bar");

        Label terms = new Label("By continuing, you confirm that the booking details above are correct.");
        terms.setWrapText(true);
        terms.getStyleClass().add("secure-note");

        VBox content = UIComponents.pageContent(
                UIComponents.bookingSteps(2),
                intro,
                details,
                checkoutBar,
                terms
        );
        root.setCenter(UIComponents.scrollable(content));
    }

    private HBox confirmationRow(String labelText, String valueText) {
        Label label = new Label(labelText.toUpperCase());
        label.getStyleClass().add("confirmation-label");
        label.setMinWidth(150);
        Label value = new Label(valueText);
        value.setWrapText(true);
        value.getStyleClass().add("confirmation-value");
        HBox.setHgrow(value, Priority.ALWAYS);
        HBox row = new HBox(24, label, value);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("confirmation-row");
        return row;
    }

    public Pane getRoot() {
        return root;
    }
}

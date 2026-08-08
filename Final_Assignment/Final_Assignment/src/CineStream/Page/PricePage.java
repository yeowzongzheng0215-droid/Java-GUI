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

public class PricePage {
    private final MainApp app;
    private final BorderPane root;

    public PricePage(MainApp app) {
        this.app = app;
        root = UIComponents.createPage(app, "movies");

        UserSession session = UserSession.getInstance();
        int seatQuantity = session.getSelectedSeats().size();
        double unitPrice = ReceiptGenerator.PRICE_PER_SEAT;
        double subtotal = seatQuantity * unitPrice;
        String seatNames = session.getSelectedSeats().stream()
                .map(Seat::getSeatNumber)
                .collect(Collectors.joining(", "));

        VBox intro = UIComponents.pageIntro(
                "Step 3 of 5",
                "Review your booking",
                "Check your movie, showtime and seats before moving to confirmation."
        );

        HBox bookingCard = new HBox(24);
        bookingCard.setAlignment(Pos.CENTER_LEFT);
        bookingCard.setPadding(new Insets(24));
        bookingCard.getStyleClass().addAll("glass-card", "review-movie-card");

        VBox poster = new VBox(UIComponents.posterFor(session.getSelectedMovie(), 180, 240));
        VBox details = new VBox(16,
                UIComponents.infoRow("Movie", session.getSelectedMovie().getTitle()),
                UIComponents.infoRow("Cinema", "CineStream Pavilion • " + session.getSelectedShowtime().getHall().getName()),
                UIComponents.infoRow("Date & time", session.getSelectedShowtime().getTime()
                        .format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy • h:mm a"))),
                UIComponents.infoRow("Seats", seatNames)
        );
        HBox.setHgrow(details, Priority.ALWAYS);
        bookingCard.getChildren().addAll(poster, details);

        VBox priceCard = new VBox(16);
        priceCard.setPadding(new Insets(26));
        priceCard.setPrefWidth(360);
        priceCard.getStyleClass().addAll("glass-card", "price-breakdown-card");
        Label priceTitle = new Label("Price breakdown");
        priceTitle.getStyleClass().add("section-heading-small");
        priceCard.getChildren().addAll(
                priceTitle,
                line("Standard ticket × " + seatQuantity, String.format("RM %.2f", subtotal)),
                line("Online booking fee", "RM 0.00"),
                separator(),
                totalLine("Total payable", String.format("RM %.2f", subtotal))
        );

        Button next = UIComponents.primaryButton("Continue  →");
        next.setMaxWidth(Double.MAX_VALUE);
        next.setOnAction(e -> app.navigateTo(new ConfirmPage(app).getRoot()));
        Button back = UIComponents.secondaryButton("Change seats");
        back.setMaxWidth(Double.MAX_VALUE);
        back.setOnAction(e -> app.navigateTo(new SeatPage(app).getRoot()));
        Label secure = new Label("🔒  Your selection is kept while you review this page.");
        secure.setWrapText(true);
        secure.getStyleClass().add("secure-note");
        priceCard.getChildren().addAll(next, back, secure);

        FlowPane layout = new FlowPane(22, 22, bookingCard, priceCard);
        layout.setAlignment(Pos.TOP_CENTER);
        bookingCard.setPrefWidth(700);

        VBox content = UIComponents.pageContent(
                UIComponents.bookingSteps(2),
                intro,
                layout
        );
        root.setCenter(UIComponents.scrollable(content));
    }

    private HBox line(String name, String value) {
        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("summary-line-label");
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("summary-line-value");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox row = new HBox(10, nameLabel, spacer, valueLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private HBox totalLine(String name, String value) {
        HBox row = line(name, value);
        row.getStyleClass().add("grand-total-row");
        for (javafx.scene.Node node : row.getChildren()) {
            node.getStyleClass().add("grand-total-text");
        }
        return row;
    }

    private Region separator() {
        Region line = new Region();
        line.getStyleClass().add("soft-separator");
        return line;
    }

    public Pane getRoot() {
        return root;
    }
}

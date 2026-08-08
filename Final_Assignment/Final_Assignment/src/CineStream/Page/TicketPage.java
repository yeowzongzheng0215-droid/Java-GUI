package CineStream.Page;

import CineStream.MainApp;
import CineStream.Model.ReceiptGenerator;
import CineStream.Model.Ticket;
import CineStream.UIComponents;
import CineStream.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class TicketPage {
    private final MainApp app;
    private final BorderPane root;
    private final Ticket ticket;

    public TicketPage(MainApp app) {
        this.app = app;
        ticket = ReceiptGenerator.generateTicket(UserSession.getInstance());
        root = UIComponents.createPage(app, "movies");

        VBox intro = UIComponents.pageIntro(
                "Booking confirmed",
                "Your e-ticket is ready!",
                "A confirmation has been created for your booking. Present this ticket at the cinema entrance."
        );

        HBox ticketCard = createTicketCard();
        Button print = UIComponents.primaryButton("Print ticket");
        print.setOnAction(e -> UIComponents.showAlert(Alert.AlertType.INFORMATION,
                "Ticket ready to print", "Ticket " + ticket.getTicketId() + " was sent to the print queue."));
        Button home = UIComponents.secondaryButton("Return to home");
        home.setOnAction(e -> app.navigateTo(new HomePage(app).getRoot()));
        Button history = UIComponents.ghostButton("View my bookings");
        history.setOnAction(e -> app.navigateTo(new BookingHistoryPage(app).getRoot()));
        HBox actions = new HBox(12, print, home, history);
        actions.setAlignment(Pos.CENTER);

        VBox content = UIComponents.pageContent(
                UIComponents.bookingSteps(4),
                intro,
                ticketCard,
                actions
        );
        root.setCenter(UIComponents.scrollable(content));
    }

    private HBox createTicketCard() {
        VBox main = new VBox(22);
        main.setPadding(new Insets(32));
        main.getStyleClass().add("ticket-main");
        HBox.setHgrow(main, Priority.ALWAYS);

        Label brand = new Label("CINESTREAM");
        brand.getStyleClass().add("ticket-brand");
        Label status = new Label("CONFIRMED");
        status.getStyleClass().add("success-chip");
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        HBox ticketHeader = new HBox(12, brand, headerSpacer, status);
        ticketHeader.setAlignment(Pos.CENTER_LEFT);

        Label movie = new Label(ticket.getMovieTitle());
        movie.setWrapText(true);
        movie.getStyleClass().add("ticket-movie-title");

        GridPane details = new GridPane();
        details.setHgap(38);
        details.setVgap(20);
        details.add(UIComponents.infoRow("Date & time", ticket.getShowtimeStr()), 0, 0);
        details.add(UIComponents.infoRow("Hall", ticket.getHallName()), 1, 0);
        details.add(UIComponents.infoRow("Seats", ticket.getSeatNumbers()), 0, 1);
        details.add(UIComponents.infoRow("Guest", ticket.getUsername()), 1, 1);

        Label barcode = new Label("▌▌ ▌ ▌▌▌ ▌▌ ▌ ▌▌ ▌▌▌ ▌ ▌▌ ▌");
        barcode.getStyleClass().add("barcode");
        Label ticketId = new Label(ticket.getTicketId());
        ticketId.getStyleClass().add("ticket-id");
        VBox barcodeGroup = new VBox(4, barcode, ticketId);
        barcodeGroup.setAlignment(Pos.CENTER_LEFT);

        Label totalCaption = new Label("TOTAL PAID");
        totalCaption.getStyleClass().add("detail-label");
        Label total = new Label(String.format("RM %.2f", ticket.getTotalPrice()));
        total.getStyleClass().add("ticket-total");
        VBox price = new VBox(4, totalCaption, total);
        price.setAlignment(Pos.CENTER_RIGHT);
        Region priceSpacer = new Region();
        HBox.setHgrow(priceSpacer, Priority.ALWAYS);
        HBox footer = new HBox(16, barcodeGroup, priceSpacer, price);
        footer.setAlignment(Pos.CENTER_LEFT);

        main.getChildren().addAll(ticketHeader, movie, details, separator(), footer);

        VBox stub = new VBox(18);
        stub.setAlignment(Pos.CENTER);
        stub.setPadding(new Insets(28));
        stub.setPrefWidth(270);
        stub.getStyleClass().add("ticket-stub");
        Label scan = new Label("SCAN AT ENTRANCE");
        scan.getStyleClass().add("detail-label");
        GridPane qr = createQrPattern();
        Label admit = new Label("ADMIT " + UserSession.getInstance().getSelectedSeats().size());
        admit.getStyleClass().add("admit-label");
        Label note = new Label("Please arrive 15 minutes before showtime.");
        note.setWrapText(true);
        note.setAlignment(Pos.CENTER);
        note.getStyleClass().add("muted-text");
        stub.getChildren().addAll(scan, qr, admit, note);

        HBox ticketCard = new HBox(main, stub);
        ticketCard.setMaxWidth(1040);
        ticketCard.setAlignment(Pos.CENTER);
        ticketCard.getStyleClass().add("e-ticket");
        return ticketCard;
    }

    private GridPane createQrPattern() {
        GridPane qr = new GridPane();
        qr.getStyleClass().add("qr-code");
        int seed = Math.abs(ticket.getTicketId().hashCode());
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Region pixel = new Region();
                pixel.setPrefSize(12, 12);
                boolean corner = (row < 3 && col < 3) || (row < 3 && col > 5) || (row > 5 && col < 3);
                boolean filled = corner || ((seed + row * 17 + col * 31) % 5 < 2);
                pixel.getStyleClass().add(filled ? "qr-dark" : "qr-light");
                qr.add(pixel, col, row);
            }
        }
        return qr;
    }

    private Region separator() {
        Region line = new Region();
        line.getStyleClass().add("ticket-separator");
        return line;
    }

    public Pane getRoot() {
        return root;
    }
}

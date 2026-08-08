package CineStream.Page;

import CineStream.BookingUtils;
import CineStream.MainApp;
import CineStream.Model.ReceiptGenerator;
import CineStream.Model.Seat;
import CineStream.Model.SeatMap;
import CineStream.Model.Showtime;
import CineStream.UIComponents;
import CineStream.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeatPage {
    private final MainApp app;
    private final BorderPane root;
    private final Set<Seat> selectedSeats = new HashSet<>();
    private final Label seatCount = new Label();
    private final Label totalPrice = new Label();
    private final FlowPane selectedSeatChips = new FlowPane(7, 7);
    private final Button proceedButton = UIComponents.primaryButton("Review booking  →");
    private final UserSession session = UserSession.getInstance();
    private final Showtime showtime;

    public SeatPage(MainApp app) {
        this.app = app;
        this.showtime = session.getSelectedShowtime();
        root = UIComponents.createPage(app, "movies");

        if (showtime == null) {
            VBox missing = UIComponents.pageContent(
                    UIComponents.pageIntro("Booking", "No showtime selected",
                            "Please return to the movie page and choose a screening first."),
                    backToMoviesButton()
            );
            root.setCenter(UIComponents.scrollable(missing));
            return;
        }

        restoreSelectedSeats();

        VBox intro = UIComponents.pageIntro(
                "Step 2 of 5",
                "Choose your perfect seats",
                "The screen is at the top. Select one or more available seats to update your total."
        );

        HBox movieStrip = createMovieStrip();
        VBox auditorium = createAuditorium();
        VBox summary = createSummary();

        FlowPane bookingLayout = new FlowPane(22, 22, auditorium, summary);
        bookingLayout.setAlignment(Pos.TOP_CENTER);
        bookingLayout.getStyleClass().add("seat-booking-layout");

        VBox content = UIComponents.pageContent(
                UIComponents.bookingSteps(1),
                intro,
                movieStrip,
                bookingLayout
        );
        root.setCenter(UIComponents.scrollable(content));
        updateSummary();
    }

    private HBox createMovieStrip() {
        Label format = new Label("IMAX EXPERIENCE");
        format.getStyleClass().add("format-chip");
        Label movie = new Label(showtime.getMovie().getTitle());
        movie.getStyleClass().add("strip-movie-title");
        Label details = new Label(showtime.getTime().format(DateTimeFormatter.ofPattern("EEEE, d MMM yyyy • h:mm a"))
                + "   |   " + showtime.getHall().getName());
        details.getStyleClass().add("muted-text");
        VBox text = new VBox(5, format, movie, details);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button change = UIComponents.ghostButton("Change showtime");
        change.setOnAction(e -> goBackToMovies());

        HBox strip = new HBox(18, text, spacer, change);
        strip.setAlignment(Pos.CENTER_LEFT);
        strip.getStyleClass().add("movie-info-strip");
        return strip;
    }

    private VBox createAuditorium() {
        Label screenLabel = new Label("SCREEN");
        screenLabel.getStyleClass().add("screen-label");
        Region screen = new Region();
        screen.getStyleClass().add("cinema-screen");
        VBox screenGroup = new VBox(7, screenLabel, screen);
        screenGroup.setAlignment(Pos.CENTER);

        GridPane seatGrid = new GridPane();
        seatGrid.setHgap(8);
        seatGrid.setVgap(8);
        seatGrid.setAlignment(Pos.CENTER);
        seatGrid.getStyleClass().add("seat-grid");

        SeatMap map = showtime.getHall().getSeatMap();
        Seat[][] seats = map.getSeats();
        int rows = seats.length;
        int cols = rows == 0 ? 0 : seats[0].length;
        int aisleAfter = cols / 2;

        for (int row = 0; row < rows; row++) {
            Label leftRow = rowLabel(row);
            seatGrid.add(leftRow, 0, row);
            for (int col = 0; col < cols; col++) {
                Seat seat = seats[row][col];
                Button button = new Button(seat.getSeatNumber());
                button.getStyleClass().add("seat-button");
                button.setPrefSize(48, 42);
                button.setMinSize(42, 38);
                updateSeatStyle(button, seat.getStatus());
                button.setDisable(seat.getStatus() == Seat.Status.BOOKED);
                button.setOnAction(e -> toggleSeat(seat, button));
                UIComponents.addHoverScale(button, 1.10);

                int gridColumn = col + 1 + (col >= aisleAfter ? 1 : 0);
                seatGrid.add(button, gridColumn, row);
            }
            int rightColumn = cols + 2;
            seatGrid.add(rowLabel(row), rightColumn, row);
        }

        Region aisle = new Region();
        aisle.setMinWidth(18);
        seatGrid.add(aisle, aisleAfter + 1, 0, 1, Math.max(rows, 1));

        HBox legend = new HBox(20,
                legend("Available", "legend-available"),
                legend("Selected", "legend-selected"),
                legend("Booked", "legend-booked")
        );
        legend.setAlignment(Pos.CENTER);

        VBox card = new VBox(28, screenGroup, seatGrid, legend);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(28));
        card.setPrefWidth(720);
        card.getStyleClass().addAll("glass-card", "auditorium-card");
        return card;
    }

    private VBox createSummary() {
        Label title = new Label("Your selection");
        title.getStyleClass().add("section-heading-small");
        Label hint = new Label("Selected seats");
        hint.getStyleClass().add("detail-label");
        selectedSeatChips.getStyleClass().add("selected-seat-list");

        seatCount.getStyleClass().add("summary-line-value");
        totalPrice.getStyleClass().add("summary-total");

        HBox countRow = summaryRow("Tickets", seatCount);
        HBox feeRow = summaryRow("Booking fee", valueLabel("RM 0.00"));
        HBox totalRow = summaryRow("Total", totalPrice);
        totalRow.getStyleClass().add("total-row");

        proceedButton.setMaxWidth(Double.MAX_VALUE);
        proceedButton.setOnAction(e -> proceed());
        Button back = UIComponents.secondaryButton("Back to showtimes");
        back.setMaxWidth(Double.MAX_VALUE);
        back.setOnAction(e -> goBackToMovies());

        Label note = new Label("Seats are confirmed only after successful payment.");
        note.setWrapText(true);
        note.getStyleClass().add("secure-note");

        VBox summary = new VBox(16, title, hint, selectedSeatChips, countRow, feeRow, totalRow,
                proceedButton, back, note);
        summary.setPadding(new Insets(26));
        summary.setPrefWidth(330);
        summary.getStyleClass().addAll("glass-card", "seat-summary-card");
        return summary;
    }

    private HBox summaryRow(String name, Label value) {
        Label label = new Label(name);
        label.getStyleClass().add("summary-line-label");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox row = new HBox(10, label, spacer, value);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private Label valueLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("summary-line-value");
        return label;
    }

    private Label rowLabel(int row) {
        Label label = new Label(String.valueOf((char) ('A' + row)));
        label.getStyleClass().add("row-label");
        return label;
    }

    private HBox legend(String text, String styleClass) {
        Region dot = new Region();
        dot.getStyleClass().addAll("legend-dot", styleClass);
        Label label = new Label(text);
        label.getStyleClass().add("legend-text");
        HBox item = new HBox(7, dot, label);
        item.setAlignment(Pos.CENTER);
        return item;
    }

    private void restoreSelectedSeats() {
        for (Seat seat : session.getSelectedSeats()) {
            if (seat.getStatus() == Seat.Status.SELECTED) {
                selectedSeats.add(seat);
            }
        }
    }

    private void toggleSeat(Seat seat, Button button) {
        if (seat.getStatus() == Seat.Status.BOOKED) {
            UIComponents.showAlert(Alert.AlertType.INFORMATION, "Seat unavailable",
                    "That seat has already been booked. Please choose another one.");
            return;
        }

        if (selectedSeats.remove(seat)) {
            seat.setStatus(Seat.Status.AVAILABLE);
        } else {
            selectedSeats.add(seat);
            seat.setStatus(Seat.Status.SELECTED);
        }
        updateSeatStyle(button, seat.getStatus());
        updateSummary();
    }

    private void updateSeatStyle(Button button, Seat.Status status) {
        button.getStyleClass().removeAll("seat-available", "seat-selected", "seat-booked");
        switch (status) {
            case AVAILABLE -> button.getStyleClass().add("seat-available");
            case SELECTED -> button.getStyleClass().add("seat-selected");
            case BOOKED -> button.getStyleClass().add("seat-booked");
        }
    }

    private void updateSummary() {
        int count = selectedSeats.size();
        seatCount.setText(count + (count == 1 ? " seat" : " seats"));
        totalPrice.setText(String.format("RM %.2f", count * ReceiptGenerator.PRICE_PER_SEAT));
        proceedButton.setDisable(count == 0);

        List<Seat> ordered = selectedSeats.stream()
                .sorted(Comparator.comparingInt(Seat::getRow).thenComparingInt(Seat::getCol))
                .toList();
        session.setSelectedSeats(ordered);

        selectedSeatChips.getChildren().clear();
        if (count == 0) {
            Label empty = new Label("No seats selected yet");
            empty.getStyleClass().add("muted-text");
            selectedSeatChips.getChildren().add(empty);
            return;
        }

        ordered.forEach(seat -> {
            Label chip = new Label(seat.getSeatNumber());
            chip.getStyleClass().add("seat-chip");
            selectedSeatChips.getChildren().add(chip);
        });
    }

    private void proceed() {
        if (selectedSeats.isEmpty()) {
            UIComponents.showAlert(Alert.AlertType.WARNING, "Choose a seat",
                    "Please select at least one available seat before continuing.");
            return;
        }
        List<Seat> ordered = selectedSeats.stream()
                .sorted(Comparator.comparingInt(Seat::getRow).thenComparingInt(Seat::getCol))
                .toList();
        session.setSelectedSeats(ordered);
        app.navigateTo(new PricePage(app).getRoot());
    }

    private Button backToMoviesButton() {
        Button button = UIComponents.primaryButton("Browse showtimes");
        button.setOnAction(e -> app.navigateTo(new MoviePage(app).getRoot()));
        return button;
    }

    private void goBackToMovies() {
        BookingUtils.releaseSelection(new ArrayList<>(selectedSeats));
        session.setSelectedSeats(List.of());
        app.navigateTo(new MoviePage(app, session.getSelectedMovie()).getRoot());
    }

    public Pane getRoot() {
        return root;
    }
}

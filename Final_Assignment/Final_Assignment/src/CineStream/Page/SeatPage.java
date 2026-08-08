package CineStream.Page;

import CineStream.BookingUtils;
import CineStream.MainApp;
import CineStream.Model.Seat;
import CineStream.Model.SeatMap;
import CineStream.Model.Showtime;
import CineStream.UserSession;
import CineStream.Model.ReceiptGenerator;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

public class SeatPage {
    private final MainApp app;
    private final BorderPane root;
    // 改成 Set，避免重复
    private final Set<Seat> selectedSeats = new HashSet<>();
    private static final double PRICE_PER_SEAT = ReceiptGenerator.PRICE_PER_SEAT;

    private Label lblSeatCount;
    private Label lblTotalPrice;

    public SeatPage(MainApp app) {
        this.app = app;
        root = new BorderPane();
        root.setPadding(new Insets(6)); // 外间距更小

        UserSession session = UserSession.getInstance();
        Showtime showtime = session.getSelectedShowtime();
        SeatMap seatMap = showtime.getHall().getSeatMap();

        // 标题与信息
        Label title = new Label("🎟️ Select Your Seats");
        title.getStyleClass().add("title-label");
        title.setStyle("-fx-font-size: 14px;");

        Label info = new Label(String.format(
                "Movie: %s | Hall: %s | Time: %s",
                showtime.getMovie().getTitle(),
                showtime.getHall().getName(),
                showtime.getTime().toString().substring(0, 16)
        ));
        info.getStyleClass().add("normal-text");
        info.setStyle("-fx-font-size: 11px;");

        // 价格显示区域
        lblSeatCount = new Label("Selected Seats: 0");
        lblSeatCount.setStyle("-fx-font-size: 11px;");

        lblTotalPrice = new Label(String.format("Total Price: RM %.2f", 0.00));
        lblTotalPrice.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #2c5aa0;");

        HBox priceBox = new HBox(15, lblSeatCount, lblTotalPrice);
        priceBox.setPadding(new Insets(3, 0, 6, 0));

        // 座位网格
        GridPane seatGrid = new GridPane();
        seatGrid.setHgap(3);
        seatGrid.setVgap(3);
        seatGrid.setPadding(new Insets(4));
        seatGrid.getStyleClass().add("panel");

        Seat[][] seats = seatMap.getSeats();
        int rows = seats.length;
        int cols = rows > 0 ? seats[0].length : 0;

        // 座位按钮更小
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Seat seat = seats[r][c];
                Button seatBtn = new Button(seat.getSeatNumber());
                seatBtn.setPrefSize(24, 24);
                seatBtn.setStyle("-fx-font-size: 9px;");
                updateSeatStyle(seatBtn, seat.getStatus());

                seatBtn.setOnAction(e -> {
                    if (seat.getStatus() == Seat.Status.BOOKED) {
                        new Alert(Alert.AlertType.INFORMATION, "This seat is already taken.").show();
                        return;
                    }
                    if (selectedSeats.contains(seat)) {
                        selectedSeats.remove(seat);
                        seat.setStatus(Seat.Status.AVAILABLE);
                    } else {
                        selectedSeats.add(seat);
                        seat.setStatus(Seat.Status.SELECTED);
                    }
                    updateSeatStyle(seatBtn, seat.getStatus());
                    updatePriceDisplay();
                });

                seatGrid.add(seatBtn, c, r);
            }
        }

        // 图例
        HBox legend = new HBox(8);
        legend.setPadding(new Insets(3, 0, 6, 0));
        legend.getChildren().addAll(
                createLegend("Available", "seat-available"),
                createLegend("Selected", "seat-selected"),
                createLegend("Booked", "seat-booked")
        );

        // 底部按钮
        Button btnNext = new Button("Proceed to Summary");
        btnNext.getStyleClass().add("button-full");
        btnNext.setStyle("-fx-font-size: 11px;");
        btnNext.setOnAction(e -> {
            if (selectedSeats.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please select at least one seat first.").show();
                return;
            }
            if (BookingUtils.bookSeats(showtime, new ArrayList<>(selectedSeats))) {
                // 转换成 List 存入 session
                session.setSelectedSeats(new ArrayList<>(selectedSeats));
                app.navigateTo(new PricePage(app).getRoot());
            } else {
                new Alert(Alert.AlertType.ERROR, "Some seats have been taken, please re-select.").show();
            }
        });

        Button btnBack = new Button("Back to Showtime");
        btnBack.getStyleClass().add("button-full");
        btnBack.setStyle("-fx-font-size: 11px;");
        btnBack.setOnAction(e -> app.navigateTo(new MoviePage(app).getRoot()));

        VBox vbox = new VBox(6, title, info, priceBox, seatGrid, legend, btnNext, btnBack);
        vbox.getStyleClass().add("card");
        vbox.setPadding(new Insets(10));
        vbox.setMaxWidth(420);

        root.setCenter(vbox);
        BorderPane.setAlignment(vbox, javafx.geometry.Pos.CENTER);
    }

    private void updateSeatStyle(Button btn, Seat.Status status) {
        btn.getStyleClass().removeAll("seat-available", "seat-selected", "seat-booked");
        if (status == Seat.Status.AVAILABLE) {
            btn.getStyleClass().add("seat-available");
        } else if (status == Seat.Status.SELECTED) {
            btn.getStyleClass().add("seat-selected");
        } else if (status == Seat.Status.BOOKED) {
            btn.getStyleClass().add("seat-booked");
        }
    }

    private void updatePriceDisplay() {
        int count = selectedSeats.size();
        double total = count * PRICE_PER_SEAT;
        lblSeatCount.setText("Selected Seats: " + count);
        lblTotalPrice.setText(String.format("Total Price: RM %.2f", total));
    }

    private HBox createLegend(String text, String style) {
        Button sample = new Button();
        sample.setPrefSize(12, 12);
        sample.getStyleClass().add(style);
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 10px;");
        return new HBox(3, sample, label);
    }

    public Pane getRoot() {
        return root;
    }
}

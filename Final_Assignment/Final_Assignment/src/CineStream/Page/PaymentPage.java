package CineStream.Page;

import CineStream.BookingUtils;
import CineStream.MainApp;
import CineStream.Model.ReceiptGenerator;
import CineStream.Model.Reservation;
import CineStream.UIComponents;
import CineStream.UserSession;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class PaymentPage {
    private final MainApp app;
    private final BorderPane root;
    private final UserSession session = UserSession.getInstance();
    private final TextField cardNumber = new TextField();
    private final TextField cardName = new TextField();
    private final TextField expiry = new TextField();
    private final TextField cvv = new TextField();
    private final ToggleGroup paymentMethods = new ToggleGroup();
    private final VBox cardFields = new VBox(16);
    private final Button payButton;

    public PaymentPage(MainApp app) {
        this.app = app;
        root = UIComponents.createPage(app, "movies");

        double price = session.getSelectedSeats().size() * ReceiptGenerator.PRICE_PER_SEAT;
        payButton = UIComponents.primaryButton(String.format("Pay RM %.2f", price));

        VBox intro = UIComponents.pageIntro(
                "Step 4 of 5",
                "Secure payment",
                "Choose a payment method. This assignment uses a simulated checkout and stores no payment data."
        );

        VBox paymentCard = createPaymentForm();
        VBox orderCard = createOrderSummary(price);
        FlowPane layout = new FlowPane(22, 22, paymentCard, orderCard);
        layout.setAlignment(Pos.TOP_CENTER);

        VBox content = UIComponents.pageContent(
                UIComponents.bookingSteps(3),
                intro,
                layout
        );
        root.setCenter(UIComponents.scrollable(content));
    }

    private VBox createPaymentForm() {
        Label methodTitle = new Label("Payment method");
        methodTitle.getStyleClass().add("section-heading-small");

        ToggleButton card = paymentOption("Credit / Debit Card", "VISA  •  MC");
        ToggleButton online = paymentOption("Online Banking", "FPX");
        ToggleButton wallet = paymentOption("E-Wallet", "TNG");
        card.setSelected(true);
        HBox methods = new HBox(10, card, online, wallet);

        configureField(cardNumber, "Card number", "1234 5678 9012 3456");
        configureField(cardName, "Name on card", "AS SHOWN ON CARD");
        configureField(expiry, "Expiry date", "MM/YY");
        configureField(cvv, "Security code", "CVV");

        GridPane smallFields = new GridPane();
        smallFields.setHgap(14);
        smallFields.add(formGroup("Expiry date", expiry), 0, 0);
        smallFields.add(formGroup("CVV", cvv), 1, 0);
        GridPane.setHgrow(expiry, Priority.ALWAYS);
        GridPane.setHgrow(cvv, Priority.ALWAYS);

        cardFields.getChildren().addAll(
                formGroup("Card number", cardNumber),
                formGroup("Name on card", cardName),
                smallFields
        );

        Label alternative = new Label("You will be redirected to your selected provider after clicking Pay.");
        alternative.setWrapText(true);
        alternative.getStyleClass().add("alternative-payment-note");
        alternative.setVisible(false);
        alternative.setManaged(false);

        paymentMethods.selectedToggleProperty().addListener((obs, oldToggle, selectedToggle) -> {
            boolean isCard = selectedToggle == card;
            cardFields.setVisible(isCard);
            cardFields.setManaged(isCard);
            alternative.setVisible(!isCard);
            alternative.setManaged(!isCard);
        });

        Label privacy = new Label("🔒  Encrypted checkout • Payment details are never stored");
        privacy.setWrapText(true);
        privacy.getStyleClass().add("secure-note");

        VBox form = new VBox(20, methodTitle, methods, cardFields, alternative, privacy);
        form.setPadding(new Insets(28));
        form.setPrefWidth(700);
        form.getStyleClass().addAll("glass-card", "payment-form-card");
        return form;
    }

    private ToggleButton paymentOption(String titleText, String badgeText) {
        Label title = new Label(titleText);
        title.getStyleClass().add("payment-option-title");
        Label badge = new Label(badgeText);
        badge.getStyleClass().add("payment-option-badge");
        VBox graphic = new VBox(7, title, badge);
        graphic.setAlignment(Pos.CENTER_LEFT);

        ToggleButton option = new ToggleButton();
        option.setGraphic(graphic);
        option.setToggleGroup(paymentMethods);
        option.getStyleClass().add("payment-method-button");
        option.setOnAction(e -> {
            if (!option.isSelected()) {
                option.setSelected(true);
            }
        });
        UIComponents.addHoverScale(option, 1.025);
        return option;
    }

    private void configureField(TextField field, String accessibleName, String prompt) {
        field.setPromptText(prompt);
        field.setAccessibleText(accessibleName);
        field.getStyleClass().add("payment-field");
        field.setMaxWidth(Double.MAX_VALUE);
    }

    private VBox formGroup(String labelText, TextField field) {
        Label label = new Label(labelText);
        label.getStyleClass().add("form-label");
        VBox group = new VBox(7, label, field);
        group.setFillWidth(true);
        GridPane.setHgrow(group, Priority.ALWAYS);
        return group;
    }

    private VBox createOrderSummary(double price) {
        Label title = new Label("Order summary");
        title.getStyleClass().add("section-heading-small");
        Label movie = new Label(session.getSelectedMovie().getTitle());
        movie.setWrapText(true);
        movie.getStyleClass().add("order-movie-title");
        Label seats = new Label(session.getSelectedSeats().size() + " ticket(s) • "
                + session.getSelectedShowtime().getHall().getName());
        seats.setWrapText(true);
        seats.getStyleClass().add("muted-text");

        HBox amount = summaryLine("Total amount", String.format("RM %.2f", price));
        amount.getStyleClass().add("order-total-line");

        payButton.setMaxWidth(Double.MAX_VALUE);
        payButton.setOnAction(e -> processPayment());
        Button back = UIComponents.secondaryButton("Back to confirmation");
        back.setMaxWidth(Double.MAX_VALUE);
        back.setOnAction(e -> app.navigateTo(new ConfirmPage(app).getRoot()));

        Label demo = new Label("DEMO PAYMENT • No real transaction will be made");
        demo.setWrapText(true);
        demo.getStyleClass().add("demo-chip");

        VBox order = new VBox(16, title, movie, seats, separator(), amount, payButton, back, demo);
        order.setPadding(new Insets(28));
        order.setPrefWidth(350);
        order.getStyleClass().addAll("glass-card", "order-summary-card");
        return order;
    }

    private HBox summaryLine(String name, String value) {
        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("summary-line-label");
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("summary-total");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox row = new HBox(10, nameLabel, spacer, valueLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private Region separator() {
        Region line = new Region();
        line.getStyleClass().add("soft-separator");
        return line;
    }

    private void processPayment() {
        if (paymentMethods.getSelectedToggle() == null) {
            UIComponents.showAlert(Alert.AlertType.WARNING, "Choose a payment method",
                    "Select your preferred payment method before continuing.");
            return;
        }

        ToggleButton selected = (ToggleButton) paymentMethods.getSelectedToggle();
        boolean cardSelected = paymentMethods.getToggles().indexOf(selected) == 0;
        if (cardSelected && !validCardDetails()) {
            return;
        }

        payButton.setDisable(true);
        payButton.setText("Processing securely…");
        PauseTransition wait = new PauseTransition(Duration.millis(750));
        wait.setOnFinished(e -> completePayment());
        wait.play();
    }

    private boolean validCardDetails() {
        String number = cardNumber.getText().replaceAll("\\s+", "");
        if (!number.matches("\\d{16}")) {
            UIComponents.showAlert(Alert.AlertType.ERROR, "Check card number",
                    "Enter a 16-digit demo card number, with or without spaces.");
            return false;
        }
        if (cardName.getText().trim().length() < 2) {
            UIComponents.showAlert(Alert.AlertType.ERROR, "Cardholder name required",
                    "Enter the name shown on the card.");
            return false;
        }
        if (!expiry.getText().trim().matches("(0[1-9]|1[0-2])/\\d{2}")) {
            UIComponents.showAlert(Alert.AlertType.ERROR, "Check expiry date",
                    "Use the MM/YY format, for example 08/28.");
            return false;
        }
        if (!cvv.getText().trim().matches("\\d{3,4}")) {
            UIComponents.showAlert(Alert.AlertType.ERROR, "Check security code",
                    "Enter a 3 or 4 digit CVV.");
            return false;
        }
        return true;
    }

    private void completePayment() {
        if (!BookingUtils.bookSeats(session.getSelectedShowtime(), session.getSelectedSeats())) {
            payButton.setDisable(false);
            payButton.setText("Try payment again");
            UIComponents.showAlert(Alert.AlertType.ERROR, "Seats no longer available",
                    "One or more seats were just booked. Please return to the seat map and choose again.");
            app.navigateTo(new SeatPage(app).getRoot());
            return;
        }

        BookingHistoryPage.ALL_BOOKINGS.add(new Reservation(
                session.getUsername(),
                session.getSelectedMovie(),
                session.getSelectedShowtime(),
                session.getSelectedSeats()
        ));
        app.navigateTo(new TicketPage(app).getRoot());
    }

    public Pane getRoot() {
        return root;
    }
}

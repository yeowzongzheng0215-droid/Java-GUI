package CineStream.Page;

import CineStream.MainApp;
import CineStream.UIComponents;
import CineStream.UserStore;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.regex.Pattern;

public class SignUpPage {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@gmail\\.com$");

    private final MainApp app;
    private final BorderPane root;

    public SignUpPage(MainApp app) {
        this.app = app;
        root = UIComponents.createPage(app, "");

        StackPane artwork = UIComponents.mediaFrame(
                "/CineStream/assets/banner/auth-cinema.png",
                "AUTH BANNER • REPLACE auth-cinema.png",
                520,
                650
        );
        artwork.getStyleClass().add("auth-artwork");
        Label artBadge = new Label("FREE MEMBERSHIP");
        artBadge.getStyleClass().add("hero-badge");
        Label artTitle = new Label("Movie nights,\nmade effortless.");
        artTitle.getStyleClass().add("auth-art-title");
        Label artBody = new Label("Create your account to book seats, receive e-tickets and revisit your booking history.");
        artBody.setWrapText(true);
        artBody.getStyleClass().add("hero-subtitle");
        VBox artCopy = new VBox(14, artBadge, artTitle, artBody);
        artCopy.setMaxWidth(400);
        StackPane.setAlignment(artCopy, Pos.BOTTOM_LEFT);
        StackPane.setMargin(artCopy, new Insets(40));
        artwork.getChildren().add(artCopy);

        TextField email = new TextField();
        email.setPromptText("you@gmail.com");
        PasswordField password = new PasswordField();
        password.setPromptText("At least 6 characters");
        PasswordField confirm = new PasswordField();
        confirm.setPromptText("Enter the same password again");

        CheckBox terms = new CheckBox("I agree to the demo booking terms and privacy notice.");
        terms.setWrapText(true);

        Button register = UIComponents.primaryButton("Create account  →");
        register.setMaxWidth(Double.MAX_VALUE);
        register.setDefaultButton(true);
        register.setOnAction(e -> register(email, password, confirm, terms));

        Label existing = new Label("Already have an account?");
        existing.getStyleClass().add("muted-text");
        Hyperlink login = new Hyperlink("Sign in");
        login.setOnAction(e -> app.navigateTo(new LoginPage(app).getRoot()));
        HBox loginRow = new HBox(5, existing, login);
        loginRow.setAlignment(Pos.CENTER);

        Label eyebrow = new Label("JOIN CINESTREAM");
        eyebrow.getStyleClass().add("eyebrow");
        Label title = new Label("Create your account");
        title.getStyleClass().add("auth-title");
        Label subtitle = new Label("It is free and takes less than a minute.");
        subtitle.getStyleClass().add("muted-text");

        VBox form = new VBox(16,
                eyebrow, title, subtitle,
                formGroup("Gmail address", email),
                formGroup("Password", password),
                formGroup("Confirm password", confirm),
                terms, register, loginRow
        );
        form.setPadding(new Insets(38, 42, 38, 42));
        form.setPrefWidth(490);
        form.getStyleClass().addAll("glass-card", "auth-form");

        FlowPane layout = new FlowPane(24, 24, artwork, form);
        layout.setAlignment(Pos.CENTER);
        VBox content = UIComponents.pageContent(layout);
        root.setCenter(UIComponents.scrollable(content));
    }

    private VBox formGroup(String text, TextField field) {
        Label label = new Label(text);
        label.getStyleClass().add("form-label");
        field.getStyleClass().add("auth-field");
        field.setMaxWidth(Double.MAX_VALUE);
        return new VBox(7, label, field);
    }

    private void register(TextField emailField, PasswordField passwordField,
                          PasswordField confirmField, CheckBox terms) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirm = confirmField.getText();

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            UIComponents.showAlert(Alert.AlertType.ERROR, "Invalid email",
                    "Please use a valid Gmail address, for example name@gmail.com.");
            return;
        }
        if (password.length() < 6) {
            UIComponents.showAlert(Alert.AlertType.ERROR, "Password too short",
                    "Use at least 6 characters for your password.");
            return;
        }
        if (!password.equals(confirm)) {
            UIComponents.showAlert(Alert.AlertType.ERROR, "Passwords do not match",
                    "Enter the same password in both password fields.");
            return;
        }
        if (!terms.isSelected()) {
            UIComponents.showAlert(Alert.AlertType.WARNING, "Accept the terms",
                    "Please accept the demo booking terms before creating your account.");
            return;
        }
        if (UserStore.getInstance().isRegistered(email)) {
            UIComponents.showAlert(Alert.AlertType.ERROR, "Account already exists",
                    "This email is already registered. Please sign in instead.");
            return;
        }

        UserStore.getInstance().register(email, password);
        UIComponents.showAlert(Alert.AlertType.INFORMATION, "Account created!",
                "Your CineStream account is ready. Sign in to start booking.");
        app.navigateTo(new LoginPage(app).getRoot());
    }

    public Pane getRoot() {
        return root;
    }
}

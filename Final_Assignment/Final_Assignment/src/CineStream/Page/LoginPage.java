package CineStream.Page;

import CineStream.MainApp;
import CineStream.UIComponents;
import CineStream.UserSession;
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

public class LoginPage {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@gmail\\.com$");

    private final MainApp app;
    private final BorderPane root;

    public LoginPage(MainApp app) {
        this.app = app;
        root = UIComponents.createPage(app, "");

        StackPane artwork = UIComponents.mediaFrame(
                "/CineStream/assets/banner/auth-cinema.png",
                "AUTH BANNER • REPLACE auth-cinema.png",
                560,
                610
        );
        artwork.getStyleClass().add("auth-artwork");
        Label artBadge = new Label("MEMBERS GET MORE");
        artBadge.getStyleClass().add("hero-badge");
        Label artTitle = new Label("Skip the queue.\nKeep the memories.");
        artTitle.getStyleClass().add("auth-art-title");
        Label artBody = new Label("Sign in to reserve your favourite seats and keep every e-ticket in one place.");
        artBody.setWrapText(true);
        artBody.getStyleClass().add("hero-subtitle");
        VBox artCopy = new VBox(14, artBadge, artTitle, artBody);
        artCopy.setMaxWidth(410);
        StackPane.setAlignment(artCopy, Pos.BOTTOM_LEFT);
        StackPane.setMargin(artCopy, new Insets(40));
        artwork.getChildren().add(artCopy);

        TextField email = new TextField();
        email.setPromptText("you@gmail.com");
        PasswordField password = new PasswordField();
        password.setPromptText("Enter your password");

        VBox emailGroup = formGroup("Email address", email);
        VBox passwordGroup = formGroup("Password", password);

        CheckBox remember = new CheckBox("Remember me");
        Hyperlink forgot = new Hyperlink("Forgot password?");
        forgot.setOnAction(e -> UIComponents.showAlert(Alert.AlertType.INFORMATION,
                "Password help", "For this demo, create a new account if you no longer remember your password."));
        HBox options = new HBox(12, remember, forgot);
        options.setAlignment(Pos.CENTER_LEFT);

        Button login = UIComponents.primaryButton("Sign in  →");
        login.setMaxWidth(Double.MAX_VALUE);
        login.setDefaultButton(true);
        login.setOnAction(e -> signIn(email, password));

        Label prompt = new Label("New to CineStream?");
        prompt.getStyleClass().add("muted-text");
        Hyperlink create = new Hyperlink("Create an account");
        create.setOnAction(e -> app.navigateTo(new SignUpPage(app).getRoot()));
        HBox signupRow = new HBox(5, prompt, create);
        signupRow.setAlignment(Pos.CENTER);

        Label eyebrow = new Label("WELCOME BACK");
        eyebrow.getStyleClass().add("eyebrow");
        Label title = new Label("Sign in to your account");
        title.setWrapText(true);
        title.getStyleClass().add("auth-title");
        Label subtitle = new Label("Continue your movie night in just a few clicks.");
        subtitle.setWrapText(true);
        subtitle.getStyleClass().add("muted-text");

        VBox form = new VBox(18, eyebrow, title, subtitle, emailGroup, passwordGroup, options, login, signupRow);
        form.setPadding(new Insets(42));
        form.setPrefWidth(480);
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

    private void signIn(TextField emailField, PasswordField passwordField) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            UIComponents.showAlert(Alert.AlertType.ERROR, "Invalid email",
                    "Please enter a valid Gmail address, for example name@gmail.com.");
            return;
        }
        if (password.isBlank()) {
            UIComponents.showAlert(Alert.AlertType.ERROR, "Password required",
                    "Enter your password to continue.");
            return;
        }
        if (!UserStore.getInstance().validateLogin(email, password)) {
            UIComponents.showAlert(Alert.AlertType.ERROR, "Unable to sign in",
                    "The email or password is incorrect. Register first if you do not have an account.");
            return;
        }

        UserSession.getInstance().login(email);
        UIComponents.showAlert(Alert.AlertType.INFORMATION, "Welcome back!",
                "You are now signed in as " + email + ".");
        app.navigateTo(new HomePage(app).getRoot());
    }

    public Pane getRoot() {
        return root;
    }
}

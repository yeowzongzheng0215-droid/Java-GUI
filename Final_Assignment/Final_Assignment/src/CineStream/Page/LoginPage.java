package CineStream.Page;

import CineStream.MainApp;
import CineStream.UserSession;
import CineStream.UserStore;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.regex.Pattern;

public class LoginPage {
    private final MainApp app;
    private final BorderPane root;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@gmail\\.com$");

    public LoginPage(MainApp app) {
        this.app = app;
        root = new BorderPane();
        root.setPadding(new Insets(40));

        VBox card = new VBox(18);
        card.getStyleClass().add("card");
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(30, 40, 30, 40));

        Label title = new Label("User Login");
        title.getStyleClass().add("title-label");

        // Email row
        HBox emailRow = new HBox(10);
        emailRow.setAlignment(Pos.CENTER_LEFT);
        Label emailIcon = new Label("✉");
        TextField tfEmail = new TextField();
        tfEmail.setPromptText("Enter your Gmail address");
        tfEmail.getStyleClass().add("text-field");
        HBox.setHgrow(tfEmail, Priority.ALWAYS);
        emailRow.getChildren().addAll(emailIcon, tfEmail);

        // Password row
        HBox passRow = new HBox(10);
        passRow.setAlignment(Pos.CENTER_LEFT);
        Label passIcon = new Label("🔒");
        PasswordField pfPassword = new PasswordField();
        pfPassword.setPromptText("Enter your password");
        pfPassword.getStyleClass().add("password-field");
        HBox.setHgrow(pfPassword, Priority.ALWAYS);
        passRow.getChildren().addAll(passIcon, pfPassword);

        // Options row
        HBox optionsRow = new HBox(10);
        optionsRow.setAlignment(Pos.CENTER_LEFT);
        CheckBox chkRemember = new CheckBox("Remember me");
        Hyperlink linkForgot = new Hyperlink("Forgot Password?");
        linkForgot.setOnAction(e -> new Alert(Alert.AlertType.INFORMATION,
                "Please sign up first if you don't have an account.").show());
        optionsRow.getChildren().addAll(chkRemember, linkForgot);

        // Login button
        Button btnLogin = new Button("Sign In");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.setOnAction(e -> {
            String email = tfEmail.getText().trim();
            String password = pfPassword.getText().trim();

            if (!EMAIL_PATTERN.matcher(email).matches()) {
                new Alert(Alert.AlertType.ERROR, "Please enter a valid Gmail address.").show();
                return;
            }
            if (password.isBlank()) {
                new Alert(Alert.AlertType.ERROR, "Password cannot be empty.").show();
                return;
            }
            // ✅ 使用 validateLogin 检查邮箱+密码
            if (!UserStore.getInstance().validateLogin(email, password)) {
                new Alert(Alert.AlertType.ERROR, "Invalid email or password.").show();
                return;
            }

            UserSession.getInstance().login(email);
            new Alert(Alert.AlertType.INFORMATION, "Login successful. Welcome, " + email).show();
            app.navigateTo(new HomePage(app).getRoot());
        });

        // Back button
        Button btnBack = new Button("Back to Home");
        btnBack.setMaxWidth(Double.MAX_VALUE);
        btnBack.setOnAction(e -> app.navigateTo(new HomePage(app).getRoot()));

        card.getChildren().addAll(title, emailRow, passRow, optionsRow, btnLogin, btnBack);
        root.setCenter(card);
    }

    public Pane getRoot() {
        return root;
    }
}

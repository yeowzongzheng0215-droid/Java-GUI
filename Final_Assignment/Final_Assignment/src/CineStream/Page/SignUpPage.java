package CineStream.Page;

import CineStream.MainApp;
import CineStream.UserStore;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.regex.Pattern;

public class SignUpPage {
    private final MainApp app;
    private final BorderPane root;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@gmail\\.com$");

    public SignUpPage(MainApp app) {
        this.app = app;
        root = new BorderPane();
        root.setPadding(new Insets(40));

        VBox card = new VBox(18);
        card.getStyleClass().add("card");
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(30, 40, 30, 40));

        Label title = new Label("Create New Account");
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
        pfPassword.setPromptText("Create password");
        pfPassword.getStyleClass().add("password-field");
        HBox.setHgrow(pfPassword, Priority.ALWAYS);
        passRow.getChildren().addAll(passIcon, pfPassword);

        // Confirm password row
        HBox confirmRow = new HBox(10);
        confirmRow.setAlignment(Pos.CENTER_LEFT);
        Label confirmIcon = new Label("🔒");
        PasswordField pfConfirm = new PasswordField();
        pfConfirm.setPromptText("Confirm password");
        pfConfirm.getStyleClass().add("password-field");
        HBox.setHgrow(pfConfirm, Priority.ALWAYS);
        confirmRow.getChildren().addAll(confirmIcon, pfConfirm);

        // Register button
        Button btnRegister = new Button("REGISTER");
        btnRegister.getStyleClass().add("button");
        btnRegister.setMaxWidth(Double.MAX_VALUE);
        btnRegister.setOnAction(e -> {
            String email = tfEmail.getText().trim();
            String pass = pfPassword.getText().trim();
            String confirm = pfConfirm.getText().trim();

            if (!EMAIL_PATTERN.matcher(email).matches()) {
                new Alert(Alert.AlertType.ERROR, "Please enter a valid Gmail address.").show();
                return;
            }
            if (pass.isEmpty() || confirm.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Password fields cannot be empty.").show();
                return;
            }
            if (!pass.equals(confirm)) {
                new Alert(Alert.AlertType.ERROR, "Passwords do not match.").show();
                return;
            }
            if (UserStore.getInstance().isRegistered(email)) {
                new Alert(Alert.AlertType.ERROR, "This email is already registered. Please login.").show();
                return;
            }

            // ✅ 注册成功 → 保存邮箱+密码
            UserStore.getInstance().register(email, pass);
            new Alert(Alert.AlertType.INFORMATION, "Registration successful! You can now log in.").show();
            app.navigateTo(new LoginPage(app).getRoot());
        });

        // Back button
        Button btnBack = new Button("Back to Home");
        btnBack.getStyleClass().add("button-secondary");
        btnBack.setMaxWidth(Double.MAX_VALUE);
        btnBack.setOnAction(e -> app.navigateTo(new HomePage(app).getRoot()));

        card.getChildren().addAll(title, emailRow, passRow, confirmRow, btnRegister, btnBack);
        root.setCenter(card);
        BorderPane.setAlignment(card, Pos.CENTER);
    }

    public Pane getRoot() {
        return root;
    }
}

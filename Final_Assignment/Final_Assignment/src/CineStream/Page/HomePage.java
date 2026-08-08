package CineStream.Page;

import CineStream.MainApp;
import CineStream.UserSession;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class HomePage {
    private final MainApp app;
    private final BorderPane root;

    public HomePage(MainApp app) {
        this.app = app;
        root = new BorderPane();
        root.setPadding(new Insets(20));

        Label title = new Label("🎬 CINESTREAM SYSTEM");
        title.getStyleClass().add("title-label");

        VBox menu = new VBox(18);
        menu.getStyleClass().add("card");
        menu.setPadding(new Insets(35));
        menu.setPrefWidth(340);

        // 未登录时显示的选项
        Button btnLogin = new Button("🔑 User Login");
        btnLogin.getStyleClass().add("button-full");
        btnLogin.setOnAction(e -> app.navigateTo(new LoginPage(app).getRoot()));

        Button btnSignUp = new Button("📝 Create Account");
        btnSignUp.getStyleClass().add("button-full");
        btnSignUp.setOnAction(e -> app.navigateTo(new SignUpPage(app).getRoot()));

        Button btnAbout = new Button("ℹ️ About System");
        btnAbout.getStyleClass().add("button-full");
        btnAbout.setOnAction(e -> app.navigateTo(new AboutPage(app).getRoot()));

        // 登录后才显示的选项
        Button btnBookTicket = new Button("🎟️ Book Movie Ticket");
        btnBookTicket.getStyleClass().add("button-full");
        btnBookTicket.setOnAction(e -> app.navigateTo(new MoviePage(app).getRoot()));

        Button btnHistory = new Button("📋 Booking History");
        btnHistory.getStyleClass().add("button-full");
        btnHistory.setOnAction(e -> {
            if (UserSession.getInstance().isLoggedIn()) {
                app.navigateTo(new BookingHistoryPage(app).getRoot());
            } else {
                new Alert(Alert.AlertType.WARNING, "Please log in first to view booking history.").show();
            }
        });

        Button btnLogout = new Button("🚪 Logout");
        btnLogout.getStyleClass().add("button-full");
        btnLogout.setOnAction(e -> {
            UserSession.getInstance().logout();
            new Alert(Alert.AlertType.INFORMATION, "You have logged out successfully.").show();
            app.navigateTo(new HomePage(app).getRoot());
        });

        // 动态显示：未登录 / 登录后
        if (UserSession.getInstance().isLoggedIn()) {
            menu.getChildren().addAll(
                title,
                btnBookTicket,
                btnHistory,
                btnAbout,
                btnLogout
            );
        } else {
            menu.getChildren().addAll(
                title,
                btnLogin,
                btnSignUp,
                btnAbout,
                btnHistory
            );
        }

        root.setCenter(menu);
        BorderPane.setAlignment(menu, javafx.geometry.Pos.CENTER);
    }

    public Pane getRoot() {
        return root;
    }
}
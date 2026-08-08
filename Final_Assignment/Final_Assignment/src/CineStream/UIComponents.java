package CineStream;

import CineStream.Model.MovieData;
import CineStream.Page.AboutPage;
import CineStream.Page.BookingHistoryPage;
import CineStream.Page.CinemaPage;
import CineStream.Page.HomePage;
import CineStream.Page.LoginPage;
import CineStream.Page.MoviePage;
import CineStream.Page.SignUpPage;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;

/**
 * Shared visual components for the application. Keeping these elements here
 * gives every screen the same navigation, spacing and interaction behaviour.
 */
public final class UIComponents {
    private UIComponents() {
    }

    public static BorderPane createPage(MainApp app, String activePage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-shell");
        root.setTop(createHeader(app, activePage));
        return root;
    }

    public static HBox createHeader(MainApp app, String activePage) {
        HBox header = new HBox(18);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("app-header");

        Button brand = new Button("CINESTREAM");
        brand.getStyleClass().add("brand-button");
        brand.setOnAction(e -> app.navigateTo(new HomePage(app).getRoot()));
        addHoverScale(brand, 1.025);

        HBox nav = new HBox(4);
        nav.setAlignment(Pos.CENTER_LEFT);
        nav.getChildren().addAll(
                navButton("Home", "home".equals(activePage), () -> app.navigateTo(new HomePage(app).getRoot())),
                navButton("Movies", "movies".equals(activePage), () -> openMovies(app)),
                navButton("Cinemas", "cinemas".equals(activePage), () -> app.navigateTo(new CinemaPage(app).getRoot())),
                navButton("About", "about".equals(activePage), () -> app.navigateTo(new AboutPage(app).getRoot()))
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox accountArea = new HBox(10);
        accountArea.setAlignment(Pos.CENTER_RIGHT);
        UserSession session = UserSession.getInstance();
        if (session.isLoggedIn()) {
            Label user = new Label("Hello, " + displayName(session.getUsername()));
            user.getStyleClass().add("user-chip");

            Button history = ghostButton("My bookings");
            history.setOnAction(e -> app.navigateTo(new BookingHistoryPage(app).getRoot()));

            Button logout = ghostButton("Log out");
            logout.setOnAction(e -> {
                session.logout();
                showAlert(Alert.AlertType.INFORMATION, "Signed out", "You have been signed out successfully.");
                app.navigateTo(new HomePage(app).getRoot());
            });

            Button book = primaryButton("Book now");
            book.setOnAction(e -> app.navigateTo(new MoviePage(app).getRoot()));
            accountArea.getChildren().addAll(user, history, logout, book);
        } else {
            Button login = ghostButton("Sign in");
            login.setOnAction(e -> app.navigateTo(new LoginPage(app).getRoot()));
            Button signUp = primaryButton("Join free");
            signUp.setOnAction(e -> app.navigateTo(new SignUpPage(app).getRoot()));
            accountArea.getChildren().addAll(login, signUp);
        }

        header.getChildren().addAll(brand, nav, spacer, accountArea);
        return header;
    }

    private static Button navButton(String text, boolean active, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add("nav-button");
        if (active) {
            button.getStyleClass().add("nav-button-active");
        }
        button.setOnAction(e -> action.run());
        return button;
    }

    private static void openMovies(MainApp app) {
        if (UserSession.getInstance().isLoggedIn()) {
            app.navigateTo(new MoviePage(app).getRoot());
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Sign in to continue",
                    "Please sign in or create a free account before booking a movie.");
            app.navigateTo(new LoginPage(app).getRoot());
        }
    }

    public static ScrollPane scrollable(Node content) {
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.getStyleClass().add("page-scroll");
        return scroll;
    }

    public static VBox pageContent(Node... children) {
        VBox content = new VBox(28, children);
        content.getStyleClass().add("page-content");
        content.setFillWidth(true);
        return content;
    }

    public static VBox pageIntro(String eyebrowText, String titleText, String subtitleText) {
        Label eyebrow = new Label(eyebrowText.toUpperCase());
        eyebrow.getStyleClass().add("eyebrow");
        Label title = new Label(titleText);
        title.setWrapText(true);
        title.getStyleClass().add("page-title");
        Label subtitle = new Label(subtitleText);
        subtitle.setWrapText(true);
        subtitle.getStyleClass().add("page-subtitle");
        VBox intro = new VBox(7, eyebrow, title, subtitle);
        intro.getStyleClass().add("page-intro");
        return intro;
    }

    public static Button primaryButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().addAll("primary-button", "interactive-button");
        addHoverScale(button, 1.035);
        return button;
    }

    public static Button secondaryButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().addAll("secondary-button", "interactive-button");
        addHoverScale(button, 1.025);
        return button;
    }

    public static Button ghostButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().addAll("ghost-button", "interactive-button");
        addHoverScale(button, 1.025);
        return button;
    }

    public static void addHoverScale(Node node, double scale) {
        node.setOnMouseEntered(e -> animateScale(node, scale));
        node.setOnMouseExited(e -> animateScale(node, 1.0));
    }

    private static void animateScale(Node node, double target) {
        ScaleTransition transition = new ScaleTransition(Duration.millis(180), node);
        transition.setToX(target);
        transition.setToY(target);
        transition.play();
    }

    public static StackPane mediaFrame(String resourcePath, String fallbackTitle,
                                       double preferredWidth, double preferredHeight) {
        StackPane frame = new StackPane();
        frame.getStyleClass().add("media-frame");
        frame.setPrefSize(preferredWidth, preferredHeight);
        frame.setMinHeight(preferredHeight);
        frame.setMaxHeight(preferredHeight);

        URL resource = UIComponents.class.getResource(resourcePath);
        if (resource != null) {
            ImageView imageView = new ImageView(new Image(resource.toExternalForm(), true));
            imageView.setPreserveRatio(false);
            imageView.fitWidthProperty().bind(frame.widthProperty());
            imageView.fitHeightProperty().bind(frame.heightProperty());
            frame.getChildren().add(imageView);
        } else {
            Label placeholder = new Label(fallbackTitle + "\n" + resourcePath);
            placeholder.setWrapText(true);
            placeholder.setAlignment(Pos.CENTER);
            placeholder.getStyleClass().add("media-placeholder-text");
            frame.getChildren().add(placeholder);
        }

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(frame.widthProperty());
        clip.heightProperty().bind(frame.heightProperty());
        clip.setArcWidth(28);
        clip.setArcHeight(28);
        frame.setClip(clip);
        return frame;
    }

    public static StackPane posterFor(MovieData movie, double width, double height) {
        String path = movie == null || movie.getPosterPath() == null || movie.getPosterPath().isBlank()
                ? "/CineStream/assets/posters/placeholder-poster.png"
                : movie.getPosterPath();
        return mediaFrame(path, movie == null ? "MOVIE POSTER" : movie.getTitle(), width, height);
    }

    public static HBox bookingSteps(int activeStep) {
        String[] steps = {"Movie", "Seats", "Summary", "Payment", "Ticket"};
        HBox bar = new HBox(8);
        bar.setAlignment(Pos.CENTER);
        bar.getStyleClass().add("step-bar");
        for (int i = 0; i < steps.length; i++) {
            Label number = new Label(String.valueOf(i + 1));
            number.getStyleClass().add("step-number");
            Label name = new Label(steps[i]);
            name.getStyleClass().add("step-name");
            HBox step = new HBox(7, number, name);
            step.setAlignment(Pos.CENTER);
            step.getStyleClass().add("step-item");
            if (i < activeStep) {
                step.getStyleClass().add("step-complete");
            } else if (i == activeStep) {
                step.getStyleClass().add("step-active");
            }
            bar.getChildren().add(step);
            if (i < steps.length - 1) {
                Region line = new Region();
                line.getStyleClass().add("step-line");
                HBox.setHgrow(line, Priority.ALWAYS);
                bar.getChildren().add(line);
            }
        }
        return bar;
    }

    public static VBox infoRow(String labelText, String valueText) {
        Label label = new Label(labelText.toUpperCase());
        label.getStyleClass().add("detail-label");
        Label value = new Label(valueText);
        value.setWrapText(true);
        value.getStyleClass().add("detail-value");
        return new VBox(4, label, value);
    }

    public static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("CineStream");
        alert.setHeaderText(title);
        alert.setContentText(message);
        if (MainApp.getPrimaryStage() != null) {
            alert.initOwner(MainApp.getPrimaryStage());
        }
        URL css = UIComponents.class.getResource("style.css");
        if (css != null) {
            alert.getDialogPane().getStylesheets().add(css.toExternalForm());
            alert.getDialogPane().getStyleClass().add("cinestream-dialog");
        }
        alert.showAndWait();
    }

    public static String displayName(String email) {
        if (email == null || email.isBlank()) {
            return "Guest";
        }
        int at = email.indexOf('@');
        String name = at > 0 ? email.substring(0, at) : email;
        if (name.length() > 16) {
            return name.substring(0, 16) + "…";
        }
        return name;
    }

    public static VBox featureCard(String icon, String titleText, String bodyText) {
        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("feature-icon");
        Label title = new Label(titleText);
        title.getStyleClass().add("card-title");
        Label body = new Label(bodyText);
        body.setWrapText(true);
        body.getStyleClass().add("muted-text");
        VBox card = new VBox(10, iconLabel, title, body);
        card.getStyleClass().addAll("glass-card", "feature-card");
        card.setPadding(new Insets(22));
        addHoverScale(card, 1.025);
        return card;
    }
}

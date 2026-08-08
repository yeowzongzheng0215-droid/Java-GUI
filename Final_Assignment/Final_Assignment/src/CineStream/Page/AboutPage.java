package CineStream.Page;

import CineStream.MainApp;
import CineStream.UIComponents;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AboutPage {
    private final MainApp app;
    private final BorderPane root;

    public AboutPage(MainApp app) {
        this.app = app;
        root = UIComponents.createPage(app, "about");

        VBox intro = UIComponents.pageIntro(
                "About the project",
                "Cinema booking, reimagined in JavaFX.",
                "CineStream is a final assignment project designed to feel like a modern Malaysian online cinema experience."
        );

        Label storyTitle = new Label("Built around the whole movie journey");
        storyTitle.getStyleClass().add("section-heading");
        Label story = new Label(
                "From discovering a film to receiving an e-ticket, every screen uses one consistent design system. "
                        + "The responsive layout adapts to the application window, while clear feedback and animated controls make each action feel immediate."
        );
        story.setWrapText(true);
        story.getStyleClass().add("about-copy");
        VBox storyCard = new VBox(14, storyTitle, story);
        storyCard.setPadding(new Insets(30));
        storyCard.setPrefWidth(700);
        storyCard.getStyleClass().addAll("glass-card", "about-story-card");

        Label version = new Label("VERSION 2.0");
        version.getStyleClass().add("hero-badge");
        Label techTitle = new Label("Powered by JavaFX");
        techTitle.getStyleClass().add("section-heading-small");
        Label tech = new Label("Responsive layouts\nReusable UI components\nInteractive seat map\nIn-memory booking flow");
        tech.getStyleClass().add("normal-text");
        VBox techCard = new VBox(14, version, techTitle, tech);
        techCard.setPadding(new Insets(30));
        techCard.setPrefWidth(340);
        techCard.getStyleClass().addAll("glass-card", "purple-card");

        FlowPane storyLayout = new FlowPane(22, 22, storyCard, techCard);
        storyLayout.setAlignment(Pos.TOP_CENTER);

        FlowPane features = new FlowPane(18, 18,
                UIComponents.featureCard("A", "Account access", "Register and sign in with validation before making a booking."),
                UIComponents.featureCard("B", "Live seat map", "Pick available seats and see the ticket total update immediately."),
                UIComponents.featureCard("C", "Complete checkout", "Review details, simulate payment and generate an e-ticket."),
                UIComponents.featureCard("D", "Booking history", "Revisit confirmed reservations made during the current session.")
        );
        features.setAlignment(Pos.CENTER);

        Button explore = UIComponents.primaryButton("Explore movies  →");
        explore.setOnAction(e -> app.navigateTo(new HomePage(app).getRoot()));
        Button cinemas = UIComponents.secondaryButton("Our cinemas");
        cinemas.setOnAction(e -> app.navigateTo(new CinemaPage(app).getRoot()));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox actions = new HBox(12, spacer, cinemas, explore);

        VBox content = UIComponents.pageContent(intro, storyLayout, features, actions);
        root.setCenter(UIComponents.scrollable(content));
    }

    public Pane getRoot() {
        return root;
    }
}

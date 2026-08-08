package CineStream.Page;

import CineStream.MainApp;
import CineStream.UIComponents;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class DatePage {
    private final MainApp app;
    private final BorderPane root;

    public DatePage(MainApp app) {
        this.app = app;
        root = UIComponents.createPage(app, "movies");

        VBox intro = UIComponents.pageIntro(
                "Plan your visit",
                "Select a screening date",
                "Choose today or an upcoming date to browse the available movies and sessions."
        );

        DatePicker picker = new DatePicker(LocalDate.now());
        picker.setDayCellFactory(control -> new DateCell() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
        picker.getStyleClass().add("date-picker-large");
        picker.setMaxWidth(Double.MAX_VALUE);

        Label label = new Label("SCREENING DATE");
        label.getStyleClass().add("detail-label");
        Button confirm = UIComponents.primaryButton("Browse showtimes  →");
        confirm.setMaxWidth(Double.MAX_VALUE);
        confirm.setOnAction(e -> {
            if (picker.getValue() == null || picker.getValue().isBefore(LocalDate.now())) {
                UIComponents.showAlert(Alert.AlertType.WARNING, "Choose a valid date",
                        "Select today or a future screening date.");
                return;
            }
            app.navigateTo(new MoviePage(app).getRoot());
        });
        Button back = UIComponents.secondaryButton("Back to home");
        back.setMaxWidth(Double.MAX_VALUE);
        back.setOnAction(e -> app.navigateTo(new HomePage(app).getRoot()));

        VBox card = new VBox(16, label, picker, confirm, back);
        card.setPadding(new Insets(32));
        card.setMaxWidth(520);
        card.getStyleClass().add("glass-card");
        card.setAlignment(Pos.CENTER_LEFT);

        VBox content = UIComponents.pageContent(intro, card);
        content.setAlignment(Pos.TOP_CENTER);
        root.setCenter(UIComponents.scrollable(content));
    }

    public Pane getRoot() {
        return root;
    }
}

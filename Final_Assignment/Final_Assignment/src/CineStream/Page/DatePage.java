package CineStream.Page;

import CineStream.MainApp;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class DatePage {
    private final MainApp app;
    private final BorderPane root;

    public DatePage(MainApp app) {
        this.app = app;
        root = new BorderPane();
        root.setPadding(new Insets(20));

        Label title = new Label("📅 Select Screening Date");
        title.getStyleClass().add("title-label");

        DatePicker datePicker = new DatePicker();
        datePicker.getStyleClass().add("text-field");

        Button btnNext = new Button("Confirm Date");
        btnNext.getStyleClass().add("button-full");
        btnNext.setOnAction(e -> app.navigateTo(new MoviePage(app).getRoot()));

        Button btnBack = new Button("Back");
        btnBack.getStyleClass().add("button-full");
        btnBack.setOnAction(e -> app.navigateTo(new HomePage(app).getRoot()));

        VBox vbox = new VBox(15, title, datePicker, btnNext, btnBack);
        vbox.getStyleClass().add("card");
        vbox.setPadding(new Insets(20));

        root.setCenter(vbox);
    }

    public Pane getRoot() {
        return root;
    }
}
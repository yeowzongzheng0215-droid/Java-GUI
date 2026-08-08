package CineStream.Page;

import CineStream.MainApp;
import CineStream.Model.ReceiptGenerator;
import CineStream.Model.Ticket;
import CineStream.UserSession;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class TicketPage {
    private final MainApp app;
    private final BorderPane root;
    private final Ticket ticket;

    public TicketPage(MainApp app) {
        this.app = app;
        this.ticket = ReceiptGenerator.generateTicket(UserSession.getInstance());
        root = new BorderPane();
        root.setPadding(new Insets(25));

        Label title = new Label("🎟️ Your E-Ticket");
        title.getStyleClass().add("title-label");

        TextArea ticketArea = new TextArea(ticket.getTicketText());
        ticketArea.setEditable(false);
        ticketArea.setPrefSize(500, 320);
        ticketArea.getStyleClass().add("ticket-text");

        Button btnPrint = new Button("🖨️ Print Ticket");
        btnPrint.getStyleClass().add("button-full");
        btnPrint.setOnAction(e -> new Alert(Alert.AlertType.INFORMATION,
                "Ticket printed successfully.\nTicket ID: " + ticket.getTicketId()).show());

        Button btnBack = new Button("Return to Home");
        btnBack.getStyleClass().add("button-full");
        btnBack.setOnAction(e -> app.navigateTo(new HomePage(app).getRoot()));

        HBox btnBox = new HBox(15, btnPrint, btnBack);
        btnBox.setPadding(new Insets(10, 0, 0, 0));

        VBox center = new VBox(15, title, ticketArea, btnBox);
        center.getStyleClass().add("card");
        root.setCenter(center);
    }

    public Pane getRoot() {
        return root;
    }
}
package CineStream.Model;

import CineStream.UserSession;
import java.util.UUID;

public class ReceiptGenerator {
    public static final double PRICE_PER_SEAT = 18.0;

    public static Ticket generateTicket(UserSession session) {
        String ticketId = "CS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new Ticket(
                ticketId,
                session.getUsername(),
                session.getSelectedMovie(),
                session.getSelectedShowtime(),
                session.getSelectedSeats(),
                PRICE_PER_SEAT
        );
    }

    public static String generateReceipt(UserSession session) {
        return generateTicket(session).getTicketText();
    }

    public static double getPricePerSeat() {
        return PRICE_PER_SEAT;
    }
}
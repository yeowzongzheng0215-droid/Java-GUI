package CineStream.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class Ticket {
    private final String ticketId;
    private final String username;
    private final String movieTitle;
    private final String hallName;
    private final String showtimeStr;
    private final String seatNumbers;
    private final String issueTime;
    private final double totalPrice;

    public Ticket(String ticketId, String username, MovieData movie, Showtime showtime,
                  java.util.List<Seat> seats, double pricePerSeat) {
        this.ticketId = ticketId;
        this.username = username;
        this.movieTitle = movie.getTitle();
        this.hallName = showtime.getHall().getName();
        this.showtimeStr = showtime.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm"));
        this.seatNumbers = seats.stream().map(Seat::getSeatNumber).collect(Collectors.joining(", "));
        this.issueTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss"));
        this.totalPrice = seats.size() * pricePerSeat;
    }

    public String getTicketText() {
        return String.format("""
============================================
            🎬 CINESTREAM E-TICKET
============================================
Ticket ID        : %s
Customer         : %s
Movie Title      : %s
Cinema Hall      : %s
Show Date & Time : %s
Seat Number(s)   : %s
Issue Time       : %s
Total Amount     : RM %.2f
============================================
Please arrive on time and enjoy your movie!
============================================
""",
                ticketId, username, movieTitle, hallName,
                showtimeStr, seatNumbers, issueTime, totalPrice
        );
    }

    public String getTicketId() { return ticketId; }
    public String getUsername() { return username; }
    public String getMovieTitle() { return movieTitle; }
    public String getHallName() { return hallName; }
    public String getShowtimeStr() { return showtimeStr; }
    public String getSeatNumbers() { return seatNumbers; }
    public String getIssueTime() { return issueTime; }
    public double getTotalPrice() { return totalPrice; }
}
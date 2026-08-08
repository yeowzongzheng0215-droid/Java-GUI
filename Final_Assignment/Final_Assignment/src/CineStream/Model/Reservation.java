package CineStream.Model;

import java.time.LocalDateTime;
import java.util.List;

public class Reservation {
    private final String username;
    private final MovieData movie;
    private final Showtime showtime;
    private final List<Seat> seats;
    private final LocalDateTime bookTime;

    public Reservation(String username, MovieData movie, Showtime showtime, List<Seat> seats) {
        this.username = username;
        this.movie = movie;
        this.showtime = showtime;
        this.seats = List.copyOf(seats);
        this.bookTime = LocalDateTime.now();
    }

    public String getUsername() { return username; }
    public MovieData getMovie() { return movie; }
    public Showtime getShowtime() { return showtime; }
    public List<Seat> getSeats() { return seats; }
    public LocalDateTime getBookTime() { return bookTime; }
}

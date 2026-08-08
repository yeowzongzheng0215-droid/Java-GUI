package CineStream.Model;

import java.time.LocalDateTime;
import java.util.List;

public class Reservation {
    private String username;
    private MovieData movie;
    private Showtime showtime;
    private List<Seat> seats;
    private LocalDateTime bookTime;

    public Reservation(String username, MovieData movie, Showtime showtime, List<Seat> seats) {
        this.username = username;
        this.movie = movie;
        this.showtime = showtime;
        this.seats = seats;
        this.bookTime = LocalDateTime.now();
    }

    public String getUsername() { return username; }
    public MovieData getMovie() { return movie; }
    public Showtime getShowtime() { return showtime; }
    public List<Seat> getSeats() { return seats; }
    public LocalDateTime getBookTime() { return bookTime; }
}
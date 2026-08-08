package CineStream.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Showtime {
    private final MovieData movie;
    private final LocalDateTime time;
    private final ScreeningHall hall;

    public Showtime(MovieData movie, LocalDateTime time, ScreeningHall hall) {
        this.movie = movie;
        this.time = time;
        this.hall = hall;
    }

    public MovieData getMovie() { return movie; }
    public LocalDateTime getTime() { return time; }
    public ScreeningHall getHall() { return hall; }

    @Override
    public String toString() {
        return String.format("%s | %s | %s",
                movie.getTitle(),
                time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                hall.getName());
    }
}
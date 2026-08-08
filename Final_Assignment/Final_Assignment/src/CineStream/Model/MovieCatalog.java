package CineStream.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * One shared in-memory catalogue. This avoids rebuilding halls and losing seat
 * state every time the user returns to the movie screen.
 */
public final class MovieCatalog {
    private static final MovieCatalog INSTANCE = new MovieCatalog();

    private final List<MovieData> movies = new ArrayList<>();
    private final List<Showtime> showtimes = new ArrayList<>();

    private MovieCatalog() {
        initialise();
    }

    public static MovieCatalog getInstance() {
        return INSTANCE;
    }

    private void initialise() {
        movies.add(new MovieData(
                "The Wandering Earth 3",
                "/CineStream/assets/posters/wandering-earth-3.png",
                "Sci-Fi • Action • Adventure",
                145));
        movies.add(new MovieData(
                "Zootopia 2",
                "/CineStream/assets/posters/zootopia-2.png",
                "Animation • Comedy • Adventure",
                110));
        movies.add(new MovieData(
                "Avengers: Final Chapter",
                "/CineStream/assets/posters/avengers-final-chapter.png",
                "Superhero • Action • Sci-Fi",
                160));

        ScreeningHall imax = new ScreeningHall("Hall 1 • IMAX", 8, 10);
        ScreeningHall fourK = new ScreeningHall("Hall 2 • 4K Dolby Atmos", 7, 9);
        ScreeningHall standard = new ScreeningHall("Hall 3 • Standard", 6, 8);

        LocalDate today = LocalDate.now();
        addShows(movies.get(0), imax, today, 10, 15, 20);
        addShows(movies.get(0), imax, today.plusDays(1), 11, 16, 21);
        addShows(movies.get(1), fourK, today, 11, 14, 18);
        addShows(movies.get(1), fourK, today.plusDays(1), 10, 15, 19);
        addShows(movies.get(2), standard, today, 12, 17, 21);
        addShows(movies.get(2), standard, today.plusDays(1), 11, 16, 20);
    }

    private void addShows(MovieData movie, ScreeningHall hall, LocalDate date, int... hours) {
        for (int hour : hours) {
            LocalTime time = LocalTime.of(hour, hour % 2 == 0 ? 0 : 30);
            showtimes.add(new Showtime(movie, LocalDateTime.of(date, time), hall));
        }
    }

    public List<MovieData> getMovies() {
        return Collections.unmodifiableList(movies);
    }

    public List<Showtime> getShowtimesFor(MovieData movie) {
        return showtimes.stream().filter(show -> show.getMovie() == movie).toList();
    }

    public List<Showtime> getShowtimes() {
        return Collections.unmodifiableList(showtimes);
    }
}

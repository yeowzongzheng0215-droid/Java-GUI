package CineStream;

import CineStream.Model.MovieData;
import CineStream.Model.Seat;
import CineStream.Model.Showtime;
import java.util.ArrayList;
import java.util.List;

public class UserSession {
    private static UserSession instance;
    private String username;
    private MovieData selectedMovie;
    private Showtime selectedShowtime;
    private final List<Seat> selectedSeats = new ArrayList<>();

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) instance = new UserSession();
        return instance;
    }

    // 登录状态
    public boolean isLoggedIn() { 
        return username != null; 
    }

    public void login(String username) { 
        this.username = username; 
    }

    public void logout() {
        // ✅ 保留单例，只清空数据
        this.username = null;
        this.selectedMovie = null;
        this.selectedShowtime = null;
        this.selectedSeats.clear();
    }

    // 用户信息
    public String getUsername() { return username; }

    // 电影选择
    public MovieData getSelectedMovie() { return selectedMovie; }
    public void setSelectedMovie(MovieData selectedMovie) { this.selectedMovie = selectedMovie; }

    // 场次选择
    public Showtime getSelectedShowtime() { return selectedShowtime; }
    public void setSelectedShowtime(Showtime selectedShowtime) { this.selectedShowtime = selectedShowtime; }

    // 座位选择
    public List<Seat> getSelectedSeats() { return selectedSeats; }
    public void setSelectedSeats(List<Seat> selectedSeats) {
        this.selectedSeats.clear();
        if (selectedSeats != null) {
            this.selectedSeats.addAll(selectedSeats);
        }
    }
}

package CineStream.Model;

public class MovieData {
    private String title;
    private String posterPath;
    private String description;
    private int duration;

    public MovieData(String title, String posterPath, String description, int duration) {
        this.title = title;
        this.posterPath = posterPath;
        this.description = description;
        this.duration = duration;
    }

    public String getTitle() { return title; }
    public String getPosterPath() { return posterPath; }
    public String getDescription() { return description; }
    public int getDuration() { return duration; }

    @Override
    public String toString() {
        return title + " (" + duration + " minute)";
    }
}
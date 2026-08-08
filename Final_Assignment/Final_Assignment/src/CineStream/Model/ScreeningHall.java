package CineStream.Model;

public class ScreeningHall {
    private static final int MAX_HALLS = 12;
    private static int hallCounter = 0;
    private final int hallId;
    private final String name;
    private final SeatMap seatMap;

    public ScreeningHall(String name, int rows, int cols) {
        if (hallCounter >= MAX_HALLS) {
            throw new IllegalStateException("系统最多支持 12 个放映厅");
        }
        this.hallId = ++hallCounter;
        this.name = name;
        this.seatMap = new SeatMap(rows, cols);
    }

    public int getHallId() { return hallId; }
    public String getName() { return name; }
    public SeatMap getSeatMap() { return seatMap; }

    @Override
    public String toString() {
        return name;
    }
}
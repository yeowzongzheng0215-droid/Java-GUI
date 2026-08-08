package CineStream.Model;

public class Seat {
    public enum Status { AVAILABLE, SELECTED, BOOKED }
    private final int row;
    private final int col;
    private Status status;

    public Seat(int row, int col) {
        this.row = row;
        this.col = col;
        this.status = Status.AVAILABLE;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getSeatNumber() {
        return String.format("%c%d", 'A' + row, col + 1);
    }
}
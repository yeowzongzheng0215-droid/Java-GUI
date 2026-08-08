package CineStream.Model;

public class SeatMap {
    private final Seat[][] seats;

    public SeatMap(int rows, int cols) {
        seats = new Seat[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                seats[r][c] = new Seat(r, c);
            }
        }
    }

    public Seat[][] getSeats() { return seats; }

    public boolean bookSeat(int row, int col) {
        if (seats[row][col].getStatus() == Seat.Status.AVAILABLE) {
            seats[row][col].setStatus(Seat.Status.BOOKED);
            return true;
        }
        return false;
    }

    public void resetAll() {
        for (Seat[] row : seats) {
            for (Seat s : row) {
                s.setStatus(Seat.Status.AVAILABLE);
            }
        }
    }
}
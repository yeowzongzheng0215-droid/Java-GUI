package CineStream;

import CineStream.Model.Seat;
import CineStream.Model.Showtime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookingUtils {
    private static final Set<String> BOOKED_KEYS = Collections.synchronizedSet(new HashSet<>());

    public static boolean bookSeats(Showtime showtime, List<Seat> seats) {
        if (showtime == null || seats.isEmpty()) return false;

        String baseKey = String.format("HALL_%d_%s",
                showtime.getHall().getHallId(),
                showtime.getTime().toString());

        for (Seat s : seats) {
            String key = baseKey + "_R" + s.getRow() + "C" + s.getCol();
            if (BOOKED_KEYS.contains(key)) return false;
        }

        for (Seat s : seats) {
            showtime.getHall().getSeatMap().bookSeat(s.getRow(), s.getCol());
            String key = baseKey + "_R" + s.getRow() + "C" + s.getCol();
            BOOKED_KEYS.add(key);
        }
        return true;
    }
}
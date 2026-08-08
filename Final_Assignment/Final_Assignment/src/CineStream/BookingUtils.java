package CineStream;

import CineStream.Model.Seat;
import CineStream.Model.Showtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class BookingUtils {
    private static final Set<String> BOOKED_KEYS = Collections.synchronizedSet(new HashSet<>());

    private BookingUtils() {
    }

    /**
     * Atomically confirms the currently selected seats at payment time.
     */
    public static boolean bookSeats(Showtime showtime, List<Seat> seats) {
        if (showtime == null || seats == null || seats.isEmpty()) {
            return false;
        }

        synchronized (BOOKED_KEYS) {
            List<String> keys = new ArrayList<>();
            for (Seat seat : seats) {
                String key = keyFor(showtime, seat);
                if (BOOKED_KEYS.contains(key) || seat.getStatus() == Seat.Status.BOOKED) {
                    return false;
                }
                keys.add(key);
            }

            for (int i = 0; i < seats.size(); i++) {
                seats.get(i).setStatus(Seat.Status.BOOKED);
                BOOKED_KEYS.add(keys.get(i));
            }
            return true;
        }
    }

    public static void releaseSelection(List<Seat> seats) {
        if (seats == null) {
            return;
        }
        for (Seat seat : seats) {
            if (seat.getStatus() == Seat.Status.SELECTED) {
                seat.setStatus(Seat.Status.AVAILABLE);
            }
        }
    }

    private static String keyFor(Showtime showtime, Seat seat) {
        return String.format("HALL_%d_%s_R%dC%d",
                showtime.getHall().getHallId(),
                showtime.getTime(),
                seat.getRow(),
                seat.getCol());
    }
}

package app.nextstep.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reservations {
    private Reservation approved;
    private Map<Long, Reservation> waiting;

    public Reservations(List<Reservation> reservations) {
        this.waiting = new HashMap<>();
        for (Reservation reservation : reservations) {
            this.waiting.put(reservation.getId(), reservation);
            this.reserveNextWaiting();
        }
    }

    private Reservation findNextWaiting() {
        Reservation earliest = null;
        for (Map.Entry<Long, Reservation> entry : this.waiting.entrySet()) {
            if (earliest == null || earliest.getId() > entry.getKey()) {
                earliest = entry.getValue();
            }
        }
        return earliest;
    }

    private void reserveNextWaiting() {
        if (this.approved != null) {
            throw new RuntimeException();
        }
        Reservation next = this.findNextWaiting();
        if (next != null) {
            this.waiting.remove(next.getId());
            this.approved = next;
        }
    }
}

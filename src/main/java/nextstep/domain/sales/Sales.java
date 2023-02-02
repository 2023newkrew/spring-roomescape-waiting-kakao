package nextstep.domain.sales;

import nextstep.domain.reservation.Reservation;

public class Sales {
    private final Reservation reservation;
    private final boolean refunded;

    public Sales(Reservation reservation, boolean refunded) {
        this.reservation = reservation;
        this.refunded = refunded;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public boolean isRefunded() {
        return refunded;
    }
}

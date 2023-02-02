package nextstep.sales;

import nextstep.reservation.Reservation;

public class Sales {
    private Long id;
    private Reservation reservation;
    private Integer amount;

    public Sales(Long id, Reservation reservation, Integer amount) {
        this.id = id;
        this.reservation = reservation;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public Integer getAmount() {
        return amount;
    }
}

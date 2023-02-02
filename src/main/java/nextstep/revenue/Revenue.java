package nextstep.revenue;

import nextstep.reservation.Reservation;

import java.time.LocalDate;

public class Revenue {
    private Long id;
    private Reservation reservation;
    private int amount;
    private LocalDate date;

    public Revenue() {
    }

    public Revenue(Reservation reservation, int amount, LocalDate date) {
        this.reservation = reservation;
        this.amount = amount;
        this.date = date;
    }

    public Revenue(Long id, Reservation reservation, int amount, LocalDate date) {
        this.id = id;
        this.reservation = reservation;
        this.amount = amount;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public int getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }
}

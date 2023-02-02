package com.nextstep.domains.sales;

import com.nextstep.domains.reservation.Reservation;
import com.nextstep.domains.sales.enums.SalesStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
public class Sales {
    @Getter
    @Setter
    private Long id;

    @Getter
    private final Reservation reservation;

    @Getter
    private final Integer amount;

    @Getter
    private SalesStatus status;

    public Long getReservationId() {
        if (Objects.isNull(reservation)) {
            return null;
        }
        return reservation.getId();
    }

    public void cancel(){
        this.status = SalesStatus.CANCEL;
    }
}

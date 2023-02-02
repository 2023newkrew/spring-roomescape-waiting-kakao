package com.nextstep.domains.reservation.dtos;

import com.nextstep.domains.reservation.entities.ReservationEntity;
import com.nextstep.domains.schedule.entities.ScheduleEntity;

public class ReservationResponse {

    private Long id;
    private ScheduleEntity schedule;

    public static ReservationResponse of(ReservationEntity reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getSchedule());
    }

    public ReservationResponse(Long id, ScheduleEntity schedule) {
        this.id = id;
        this.schedule = schedule;
    }

    public Long getId() {
        return id;
    }

    public ScheduleEntity getSchedule() {
        return schedule;
    }


}

package com.nextstep.domains.reservation.entities;

import com.nextstep.domains.member.entities.MemberEntity;
import com.nextstep.domains.schedule.entities.ScheduleEntity;

import java.util.Objects;

public class ReservationEntity {
    private Long id;
    private ScheduleEntity schedule;
    private MemberEntity member;

    public ReservationEntity() {
    }

    public ReservationEntity(ScheduleEntity schedule, MemberEntity member) {
        this.schedule = schedule;
        this.member = member;
    }

    public ReservationEntity(Long id, ScheduleEntity schedule, MemberEntity member) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public ScheduleEntity getSchedule() {
        return schedule;
    }

    public MemberEntity getMember() {
        return member;
    }

    public boolean sameMember(MemberEntity member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }
}

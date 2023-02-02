package com.nextstep.domains.waiting.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nextstep.domains.member.entities.MemberEntity;
import com.nextstep.domains.schedule.entities.ScheduleEntity;

public class WaitingEntity {
    private Long id;
    private Long waitingNumber;
    private ScheduleEntity schedule;
    @JsonIgnore
    private MemberEntity member;

    public WaitingEntity(Long id, ScheduleEntity schedule, MemberEntity member, Long waitingNumber) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.waitingNumber = waitingNumber;
    }

    public Long getId() {
        return id;
    }

    public Long getWaitingNumber() {
        return waitingNumber;
    }

    public ScheduleEntity getSchedule() {
        return schedule;
    }

    public MemberEntity getMember() {
        return member;
    }
}

package com.nextstep.interfaces.reservation.dtos;

import com.nextstep.domains.reservation.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface ReservationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "member.id", source = "memberId")
    @Mapping(target = "schedule.id", source = "request.scheduleId")
    @Mapping(target = "status", ignore = true)
    Reservation fromRequest(Long memberId, ReservationRequest request);

    ReservationResponse toResponse(Reservation reservation);

    Reservation fromResponse(ReservationResponse response);
}
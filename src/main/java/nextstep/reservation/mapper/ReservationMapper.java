package nextstep.reservation.mapper;

import nextstep.member.domain.Member;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface ReservationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "schedule.id", source = "request.scheduleId")
    Reservation fromRequest(Member member, ReservationRequest request);

    ReservationResponse toResponse(Reservation reservation);
}
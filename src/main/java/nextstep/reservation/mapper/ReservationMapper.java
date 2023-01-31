package nextstep.reservation.mapper;

import nextstep.reservation.domain.Reservation;
import nextstep.reservation.domain.ReservationEntity;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface ReservationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "member.id", source = "memberId")
    @Mapping(target = "schedule.id", source = "request.scheduleId")
    Reservation fromRequest(Long memberId, ReservationRequest request);

    ReservationResponse toResponse(ReservationEntity reservation);
}
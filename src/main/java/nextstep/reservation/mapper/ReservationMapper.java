package nextstep.reservation.mapper;

import nextstep.member.domain.MemberEntity;
import nextstep.reservation.domain.ReservationEntity;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface ReservationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "schedule.id", source = "request.scheduleId")
    ReservationEntity fromRequest(MemberEntity member, ReservationRequest request);

    ReservationResponse toResponse(ReservationEntity reservation);
}
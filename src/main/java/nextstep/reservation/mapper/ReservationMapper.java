package nextstep.reservation.mapper;

import nextstep.reservation.domain.Reservation;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface ReservationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "theme.id", source = "themeId")
    Reservation fromRequest(ReservationRequest request);

    ReservationResponse toResponse(Reservation reservation);
}
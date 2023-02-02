package com.nextstep.domains.reservation;

import com.authorizationserver.domains.authorization.enums.RoleType;
import com.authorizationserver.domains.authorization.exceptions.AuthenticationErrorMessageType;
import com.authorizationserver.domains.authorization.exceptions.AuthenticationException;
import com.authorizationserver.infrastructures.jwt.TokenData;
import com.nextstep.domains.reservation.enums.StatusType;
import com.nextstep.interfaces.reservation.dtos.ReservationRequest;
import com.nextstep.interfaces.reservation.dtos.ReservationResponse;
import com.nextstep.interfaces.reservation.dtos.ReservationMapper;
import lombok.RequiredArgsConstructor;
import com.nextstep.domains.exceptions.ErrorMessageType;
import com.nextstep.domains.exceptions.ReservationException;
import com.nextstep.interfaces.schedule.dtos.ScheduleResponse;
import com.nextstep.domains.waiting.Waiting;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReservationService {

    private final ReservationRepository repository;

    private final ReservationMapper mapper;

    @Transactional
    public ReservationResponse create(Long memberId, ReservationRequest request, ScheduleResponse schedule) {
        validateSchedule(schedule);
        Reservation reservation = mapper.fromRequest(memberId, request);
        return mapper.toResponse(repository.insert(reservation));
    }

    private void validateSchedule(ScheduleResponse schedule) {
        if (repository.existsByScheduleId(schedule.getId())) {
            throw new ReservationException(ErrorMessageType.RESERVATION_CONFLICT);
        }
    }

    public ReservationResponse getById(Long id) {
        Reservation reservation = repository.getById(id);
        return mapper.toResponse(reservation);
    }


    public List<ReservationResponse> getByMemberId(Long memberId) {
        return repository.getByMemberId(memberId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public boolean existsByMemberIdAndScheduleId(Long memberId, Long scheduleId) {
        return repository.existsByMemberIdAndScheduleId(memberId, scheduleId);
    }

    @Transactional
    public boolean deleteById(TokenData tokenData, Long id, Waiting waiting) {
        Reservation reservation = repository.getById(id);
        validateReservationMine(reservation, tokenData);
        Reservation nextReservation = mapper.fromRequest(waiting.getMemberId(), new ReservationRequest(reservation.getScheduleId()));
        if (!Objects.isNull(waiting)) {
            repository.insert(nextReservation);
        }
        return repository.deleteById(id);
    }

    @Transactional
    public boolean approveById(TokenData tokenData, Long id) {
        Reservation reservation = repository.getById(id);
        System.out.println(tokenData.getRole());
        validateReservationAdmin(reservation, tokenData);
        if (!reservation.getStatus().equals(StatusType.UNAPPROVED)){
            throw new ReservationException(ErrorMessageType.RESERVATION_STATUS_CONFLICT);
        }
        return repository.updateById(id, StatusType.APPROVED);
    }

    private void validateReservationMine(Reservation reservation, TokenData tokenData) {
        if (Objects.isNull(reservation)) {
            throw new ReservationException(ErrorMessageType.RESERVATION_NOT_EXISTS);
        }
        if (!tokenData.getId().equals(reservation.getMemberId())) {
            throw new ReservationException(ErrorMessageType.NOT_RESERVATION_OWNER);
        }
    }

    private void validateReservationAdmin(Reservation reservation, TokenData tokenData) {
        if (Objects.isNull(reservation)) {
            throw new ReservationException(ErrorMessageType.RESERVATION_NOT_EXISTS);
        }
        if (!tokenData.getRole().equals(RoleType.ADMIN.name())) {
            throw new AuthenticationException(AuthenticationErrorMessageType.NOT_ADMIN);
        }
    }
}

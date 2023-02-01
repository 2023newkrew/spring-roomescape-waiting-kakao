package com.nextstep.domains.reservation;

import com.nextstep.interfaces.reservation.dtos.ReservationRequest;
import com.nextstep.interfaces.reservation.dtos.ReservationResponse;
import com.nextstep.interfaces.reservation.dtos.ReservationMapper;
import lombok.RequiredArgsConstructor;
import com.nextstep.interfaces.exceptions.ErrorMessageType;
import com.nextstep.interfaces.exceptions.ReservationException;
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
    public boolean deleteById(Long memberId, Long id, Waiting waiting) {
        Reservation reservation = repository.getById(id);
        validateReservation(reservation, memberId);
        if (Objects.isNull(waiting)) {
            return repository.deleteById(id);
        }
        return repository.updateById(id, waiting.getMemberId());
    }

    private void validateReservation(Reservation reservation, Long memberId) {
        if (Objects.isNull(reservation)) {
            throw new ReservationException(ErrorMessageType.RESERVATION_NOT_EXISTS);
        }
        if (!memberId.equals(reservation.getMemberId())) {
            throw new ReservationException(ErrorMessageType.NOT_RESERVATION_OWNER);
        }
    }
}

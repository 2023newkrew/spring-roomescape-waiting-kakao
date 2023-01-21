package nextstep.reservation.service;

import lombok.RequiredArgsConstructor;
import nextstep.etc.exception.ErrorMessage;
import nextstep.etc.exception.ReservationException;
import nextstep.etc.exception.ThemeException;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;
import nextstep.reservation.mapper.ReservationMapper;
import nextstep.reservation.repository.ReservationRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repository;

    private final ReservationMapper mapper;

    @Transactional
    @Override
    public ReservationResponse create(ReservationRequest request) {
        Reservation reservation = mapper.fromRequest(request);
        if (repository.existsByTimetable(reservation)) {
            throw new ReservationException(ErrorMessage.RESERVATION_CONFLICT);
        }
        try {
            reservation = repository.insert(reservation);
        }
        catch (DataIntegrityViolationException ignore) {
            throw new ThemeException(ErrorMessage.THEME_NOT_EXISTS);
        }

        return mapper.toResponse(reservation);
    }

    @Override
    public ReservationResponse getById(Long id) {
        Reservation reservation = repository.getById(id);

        return mapper.toResponse(reservation);
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        return repository.deleteById(id);
    }
}

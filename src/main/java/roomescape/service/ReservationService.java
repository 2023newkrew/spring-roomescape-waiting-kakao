package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import roomescape.dto.ReservationsControllerPostBody;
import roomescape.entity.Reservation;
import roomescape.exception.AlreadyExistReservationException;
import roomescape.exception.AuthorizationException;
import roomescape.exception.NotExistReservationException;
import roomescape.repository.ReservationRepository;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository repository;


    public long createReservation(long callerId, ReservationsControllerPostBody body) {
        var id = repository.insert(body.getName(), body.getDate(), body.getTime(), body.getThemeId(), callerId);
        if (id.isEmpty()) {
            throw new AlreadyExistReservationException(body.getDate(), body.getTime());
        }
        return id.get();
    }


    public Reservation findReservation(@PathVariable Long id) {
        var reservation = repository.selectById(id);
        if (reservation.isEmpty()) {
            throw new NotExistReservationException(id);
        }
        return reservation.get();
    }

    public void deleteReservation(long callerId, Long id) {
        var reservation = repository.selectById(id);
        if (reservation.isEmpty()) {
            throw new NotExistReservationException(id);
        }
        if (reservation.get().getMemberId() != callerId) {
            throw new AuthorizationException();
        }
        repository.delete(id);
    }
}

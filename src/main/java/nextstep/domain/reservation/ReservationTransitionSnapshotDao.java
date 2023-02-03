package nextstep.domain.reservation;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationTransitionSnapshotDao {

    private List<ReservationTransitionSnapshot> failedReservationSearches = new ArrayList<>();

    public void save(ReservationTransitionSnapshot reservationTransitionSnapshot) {
        failedReservationSearches.add(reservationTransitionSnapshot);
    }

    public Optional<ReservationTransitionSnapshot> findPrevSnapshotIfExists(LocalDateTime prevStartTime) {
        return failedReservationSearches.stream()
                .filter(reservationTransitionSnapshot -> reservationTransitionSnapshot.isCreatedAfter(prevStartTime))
                .findAny();
    }

}

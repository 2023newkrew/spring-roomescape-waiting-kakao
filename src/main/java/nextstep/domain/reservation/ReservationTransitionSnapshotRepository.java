package nextstep.domain.reservation;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ReservationTransitionSnapshotRepository {

    private List<ReservationTransitionSnapshot> failedReservationSearches = new ArrayList<>();

    public void save(ReservationTransitionSnapshot reservationTransitionSnapshot) {
        failedReservationSearches.add(reservationTransitionSnapshot);
    }

    public List<ReservationTransitionSnapshot> findIsAfterPrevStartTime(LocalDateTime prevStartTime) {
        return failedReservationSearches.stream()
                .filter(reservationTransitionSnapshot -> reservationTransitionSnapshot.isCreatedAfter(prevStartTime))
                .collect(Collectors.toList());
    }

}

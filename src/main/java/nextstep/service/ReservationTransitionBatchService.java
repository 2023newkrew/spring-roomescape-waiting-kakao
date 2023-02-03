package nextstep.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.domain.reservation.*;
import nextstep.error.ApplicationException;
import nextstep.utils.batch.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static nextstep.error.ErrorType.INTERNAL_SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationTransitionBatchService {

    private final ReservationTransitionSnapshotDao reservationTransitionSnapshotDao;
    private final ReservationTransitionStatusJobConfig jobConfig;

    public void transitionReservationStatus(String statusName) {
        LocalDateTime startTime = getStartTime(LocalDateTime.now().minusMinutes(10));

        try {
            jobConfig.setStatusNameAndStartTime(statusName, startTime);
            Job job = jobConfig.transitionReservationStatusJob();
            job.run();
        } catch (Exception e) {
            log.error("[Reservation Batch] Reservation Transition Batch Failed. StartTime: {}", startTime);
            reservationTransitionSnapshotDao.save(new ReservationTransitionSnapshot(startTime));
            throw new ApplicationException(INTERNAL_SERVER_ERROR);
        }
    }

    private LocalDateTime getStartTime(LocalDateTime startTime) {
        return reservationTransitionSnapshotDao.findPrevSnapshotIfExists(startTime)
                .map(ReservationTransitionSnapshot::getStartTime)
                .orElseGet(() -> startTime);
    }

}

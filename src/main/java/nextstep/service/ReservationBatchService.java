package nextstep.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.domain.reservation.*;
import nextstep.error.ApplicationException;
import nextstep.utils.TransactionUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static nextstep.error.ErrorType.INTERNAL_SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationBatchService {

    private final static int CHUNK_SIZE = 20;

    private final ReservationDao reservationDao;
    private final ReservationTransitionSnapshotDao reservationTransitionSnapshotDao;
    private final TransactionUtil transactionUtil;

    public void transitReservationStatus(String statusName) {
        LocalDateTime start = LocalDateTime.now().minusMinutes(10);
        ReservationSearch reservationSearch = new ReservationSearch(ReservationStatus.valueOf(statusName), null, start, CHUNK_SIZE);

        recoverFailedReservations(start);

        while (true) {
            try {
                int transitionedReservationSize = transitReservationStatusByChunkSize(reservationSearch);

                if (transitionedReservationSize < CHUNK_SIZE) {
                    break;
                }
            } catch (Exception e) {
                log.error("[Reservation Batch] Reservation Transition Batch Read Failed. Start: {}", start);
                throw new ApplicationException(INTERNAL_SERVER_ERROR);
            }
        }
    }

    private void recoverFailedReservations(LocalDateTime start) {
        List<ReservationTransitionSnapshot> reservationTransitionSnapshots = reservationTransitionSnapshotDao.findIsAfterPrevStartTime(start);

        for (ReservationTransitionSnapshot reservationTransitionSnapshot : reservationTransitionSnapshots) {
            ReservationStatus status = reservationTransitionSnapshot.getReservationProjections().get(0).getStatus();
            transactionUtil.executeTask(() -> writeReservationsWithTransitionedStatus(status, reservationTransitionSnapshot.getReservationProjections()));
        }
    }

    private int transitReservationStatusByChunkSize(ReservationSearch reservationSearch) {
        return transactionUtil.executeTask(() -> {
            List<ReservationProjection> reservationProjections = readReservationsByStatus(reservationSearch);
            writeReservationsWithTransitionedStatus(reservationSearch.getNextStatus(), reservationProjections);
            reservationSearch.setLastReservationId(reservationProjections.get(reservationProjections.size() - 1).getId());

            return reservationProjections.size();
        }, false);
    }

    private List<ReservationProjection> readReservationsByStatus(ReservationSearch reservationSearch) {
        return reservationDao.findAllByStatusAndCreatedAt(reservationSearch);
    }

    private void writeReservationsWithTransitionedStatus(ReservationStatus status, List<ReservationProjection> reservationProjections) {
        try {
            reservationDao.batchUpdateReservationStatus(status.name(), reservationProjections);
        } catch (Exception e) {
            log.error("[Reservation Batch] Reservation Transition Batch Write Failed. Items: {}", reservationProjections);
            reservationTransitionSnapshotDao.save(new ReservationTransitionSnapshot(reservationProjections));
        }
    }

}

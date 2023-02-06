package nextstep.worker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.repository.ProfitDao;
import nextstep.support.exception.api.NoActiveTransactionException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;

import static nextstep.support.constant.ProfitSettings.PROFIT;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncReservationApproveEventHandler implements AsyncEventHandler<ReservationApproveEvent> {
    private final ProfitDao profitDao;

    @Async
    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAsync(ReservationApproveEvent reservationApproveEvent) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new NoActiveTransactionException();
        }

        if (reservationApproveEvent.isApproveTrue()) {
            profitDao.save(LocalDateTime.now(), PROFIT);
            reservationApproveEvent.callBack();
            return;
        }

        profitDao.save(LocalDateTime.now(), -PROFIT);
        reservationApproveEvent.callBack();
    }

}

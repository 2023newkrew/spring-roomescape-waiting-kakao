package nextstep.worker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.repository.ProfitDao;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

import static nextstep.support.constant.DepositSettings.DEPOSIT;

@Slf4j
@RequiredArgsConstructor
@Component
public class AsyncReservationApproveEventHandler {
    private final ProfitDao profitDao;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void handleAsync(ReservationApproveEvent reservationApproveEvent) {
        if (reservationApproveEvent.isApproveTrue()) {
            profitDao.save(LocalDateTime.now(), DEPOSIT);
            return;
        }
        profitDao.save(LocalDateTime.now(), -DEPOSIT);
    }
}

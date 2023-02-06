package nextstep.service;

import lombok.RequiredArgsConstructor;
import nextstep.domain.reservation.Reservation;
import nextstep.domain.saleshistory.SalesHistory;
import nextstep.domain.saleshistory.SalesHistoryDao;
import org.springframework.stereotype.Service;

import static nextstep.domain.saleshistory.SalesHistoryStatus.PAYMENT;
import static nextstep.domain.saleshistory.SalesHistoryStatus.REFUND;

@RequiredArgsConstructor
@Service
public class SalesHistoryService {

    private final SalesHistoryDao salesHistoryDao;

    public void savePaymentHistory(Reservation reservation) {
        salesHistoryDao.save(new SalesHistory(reservation.getThemeId(), reservation.getId(), reservation.getDeposit(), PAYMENT));
    }

    public void saveRefundHistory(Reservation reservation) {
        salesHistoryDao.save(new SalesHistory(reservation.getThemeId(), reservation.getId(), reservation.getDeposit(), REFUND));
    }

}

package nextstep.service;

import lombok.RequiredArgsConstructor;
import nextstep.domain.reservation.Reservation;
import nextstep.domain.saleshistory.SalesHistory;
import nextstep.domain.saleshistory.SalesHistoryDao;
import org.springframework.stereotype.Service;

import static nextstep.domain.saleshistory.SalesHistoryStatus.PAYMENT;

@RequiredArgsConstructor
@Service
public class SalesHistoryService {

    private final SalesHistoryDao salesHistoryDao;

    public void saveHistory(Reservation reservation) {
        salesHistoryDao.save(new SalesHistory(reservation.getThemeId(), reservation.getDeposit(), PAYMENT));
    }

}

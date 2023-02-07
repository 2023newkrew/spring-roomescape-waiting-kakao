package nextstep.revenue;

import nextstep.reservation.Reservation;
import nextstep.support.NotExistEntityException;
import org.springframework.stereotype.Service;

@Service
public class RevenueService {
    private final RevenueDao revenueDao;

    public RevenueService(final RevenueDao revenueDao) {
        this.revenueDao = revenueDao;
    }

    public Long create(Revenue revenue) {
        return revenueDao.save(revenue);
    }

    public void refund(Reservation reservation) {
        Revenue revenue = revenueDao.findByReservationId(reservation.getId()).orElseThrow(NotExistEntityException::new);
        revenueDao.save(Revenue.builder()
                .id(revenue.getId())
                .reservationId(revenue.getReservationId())
                .price(-revenue.getPrice())
                .build());
    }
}

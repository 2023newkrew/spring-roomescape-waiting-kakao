package nextstep.revenue;

import nextstep.support.NotExistEntityException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RevenueService {
    private final RevenueDao revenueDao;

    public RevenueService(final RevenueDao revenueDao) {
        this.revenueDao = revenueDao;
    }

    public Long create(Revenue revenue) {
        return revenueDao.save(revenue);
    }

    public void deleteByReservationId(Long reservationId) {
        Revenue revenue = revenueDao.findByReservationId(reservationId).orElseThrow(NotExistEntityException::new);
        revenueDao.deleteById(revenue.getId());
    }
}

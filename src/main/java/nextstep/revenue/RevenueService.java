package nextstep.revenue;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationService;
import nextstep.reservation.ReservationState;
import nextstep.schedule.Schedule;
import nextstep.support.DuplicateEntityException;
import nextstep.support.NotExistEntityException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RevenueService {
    private final RevenueDao revenueDao;

    public RevenueService(final RevenueDao revenueDao) {
        this.revenueDao = revenueDao;
    }

    public Long create(Revenue revenue) {
        return revenueDao.save(revenue);
    }
}

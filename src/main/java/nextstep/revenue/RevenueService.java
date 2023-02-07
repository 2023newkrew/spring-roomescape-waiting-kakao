package nextstep.revenue;

import java.util.List;
import java.util.Objects;
import nextstep.common.annotation.AdminRequired;
import nextstep.error.ErrorCode;
import nextstep.error.exception.RoomReservationException;
import nextstep.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RevenueService {

    private final RevenueDao revenueDao;

    public RevenueService(RevenueDao revenueDao) {
        this.revenueDao = revenueDao;
    }

    @AdminRequired
    public List<Revenue> findAll(Member member) {
        return revenueDao.findAll();
    }

    @AdminRequired
    public Revenue findById(Member member, Long id) {
        Revenue revenue = revenueDao.findById(id);
        if (Objects.isNull(revenue)) {
            throw new RoomReservationException(ErrorCode.REVENUE_NOT_FOUND);
        }
        return revenue;
    }
}

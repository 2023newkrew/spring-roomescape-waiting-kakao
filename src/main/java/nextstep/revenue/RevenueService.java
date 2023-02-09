package nextstep.revenue;

import java.util.List;
import java.util.Objects;
import nextstep.common.annotation.AdminRequired;
import nextstep.error.ErrorCode;
import nextstep.error.exception.RoomReservationException;
import nextstep.member.Member;
import nextstep.reservation.event.ReservationApproveCancelEvent;
import nextstep.reservation.event.ReservationApproveEvent;
import nextstep.reservation.event.ReservationRefuseEvent;
import org.springframework.context.event.EventListener;
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

    @EventListener
    public void handleReservationApproveEvent(ReservationApproveEvent event) {
        Revenue revenue = new Revenue(event.getReservation().getSchedule().getTheme().getPrice());
        revenueDao.save(revenue);
        event.getReservation().setRevenue(revenue);
    }

    @EventListener
    public void handleReservationRefuseEvent(ReservationRefuseEvent event) {
        Revenue revenue = event.getReservation().getRevenue();
        if (Objects.isNull(revenue)) {
            return;
        }
        revenue.refund();
        revenueDao.save(revenue);
    }

    @EventListener
    public void handleReservationApproveCancelEvent(ReservationApproveCancelEvent event) {
        Revenue revenue = event.getReservation().getRevenue();
        if (Objects.isNull(revenue)) {
            throw new RoomReservationException(ErrorCode.REVENUE_NOT_FOUND);
        }
        revenue.refund();
        revenueDao.save(revenue);
    }
}

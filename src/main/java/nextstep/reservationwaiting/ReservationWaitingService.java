package nextstep.reservationwaiting;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.NotExistEntityException;
import nextstep.theme.ThemeDao;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationWaitingService {
    public final ReservationDao reservationDao;
    public final ReservationWaitingDao reservationWaitingDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public ReservationWaitingService(ReservationDao reservationDao, ReservationWaitingDao reservationWaitingDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.reservationWaitingDao = reservationWaitingDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
    }

    @Transactional
    public Long reserve(Member member, ReservationWaitingRequest reservationRequest) {
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId())
                .orElseThrow(NotExistEntityException::new);

        LocalDateTime now = LocalDateTime.now();
        ReservationWaiting reservationWaiting = new ReservationWaiting(schedule, member, now);
        try {
            reservationDao.save(new Reservation(schedule, member));
            reservationWaiting.reserved();
        } catch (DuplicateKeyException e) {
            reservationWaiting.waiting();
        }
        Long id = reservationWaitingDao.save(reservationWaiting);
        return id;
    }

    public List<ReservationWaitingResponse> findMyReservationWaitings(Member member) {
        List<ReservationWaitingResponse> res = reservationWaitingDao.findAllByMemberIdWithOrder(member.getId()).stream()
                .map(ReservationWaitingResponse::fromEntity)
                .collect(Collectors.toList());
        return res;
    }

    public void cancelById(Member member, Long id) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(id)
                .orElseThrow(NotExistEntityException::new);

        if (!reservationWaiting.isReservedBy(member)) {
            throw new AuthenticationException();
        }
        if (!reservationWaitingDao.cancelById(id)) {
            throw new NotExistEntityException();
        };
    }
}

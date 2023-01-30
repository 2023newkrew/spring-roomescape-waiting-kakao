package nextstep.reservationwaiting;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.ThemeDao;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

    public Long create(Member member, ReservationWaitingRequest reservationRequest) {
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }
        ReservationWaitingStatus currentStatus = ReservationWaitingStatus.WAITING;
        if(tryInsertReservation(schedule, member)) {
            currentStatus = ReservationWaitingStatus.RESERVED;
        }
        Long id = reservationWaitingDao.save(new ReservationWaiting(schedule, member, currentStatus));
        return id;
    }

    private boolean tryInsertReservation(Schedule schedule, Member member) {
        try {
            reservationDao.save(new Reservation(schedule, member));
        } catch (DuplicateKeyException e) {
            // 예약이 이미 존재할 때
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<ReservationWaitingResponse> findMyReservationWaitings(Member member) {
        try {
            return reservationWaitingDao.findAllByMemberId(member.getId()).stream()
                    .map(r -> {
                        Long rank = reservationWaitingDao.getRank(r);
                        return new ReservationWaitingResponse(r.getId(), r.getSchedule(), rank);
                    }).collect(Collectors.toList());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}

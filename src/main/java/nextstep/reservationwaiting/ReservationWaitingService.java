package nextstep.reservationwaiting;

import auth.AuthenticationException;
import nextstep.exception.NotExistEntityException;
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
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId())
                .orElseThrow(() -> new NotExistEntityException(ScheduleDao.class));
        ReservationWaitingStatus currentStatus = ReservationWaitingStatus.WAITING;
        if (tryInsertReservation(schedule, member)) {
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
            System.out.println("예약이 이미 존재합니다. 예약 대기 상태로 등록합니다.");
            return false;
        }
        return true;
    }

    public List<ReservationWaitingResponse> findMyReservationWaitings(Member member) {
        try {
            List<ReservationWaitingResponse> res = reservationWaitingDao.findAllByMemberIdWithOrder(member.getId()).stream()
                    .map(ReservationWaitingResponse::from)
                    .collect(Collectors.toList());
            return res;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void cancelById(Member member, Long id) {
        ReservationWaitingStatus status = ReservationWaitingStatus.CANCELED;
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(id)
                .orElseThrow(() -> new NotExistEntityException(Reservation.class));

        if (!reservationWaiting.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationWaitingDao.updateStatusById(id, status);
    }
}

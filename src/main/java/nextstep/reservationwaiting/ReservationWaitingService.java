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

    public Long reserve(Member member, ReservationWaitingRequest reservationRequest) {
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId())
                .orElseThrow(NotExistEntityException::new);

        ReservationWaitingStatus currentStatus = ReservationWaitingStatus.WAITING;
        try {
            reservationDao.save(new Reservation(schedule, member));
            currentStatus = ReservationWaitingStatus.RESERVED;
        } catch (DuplicateKeyException e) {
            System.out.println("예약이 이미 존재하여 예약 대기열로 이동합니다.");
        }
        LocalDateTime now = LocalDateTime.now();
        Long id = reservationWaitingDao.save(new ReservationWaiting(schedule, member, currentStatus, now));
        return id;
    }

    public List<ReservationWaitingResponse> findMyReservationWaitings(Member member) {
        List<ReservationWaitingResponse> res = reservationWaitingDao.findAllByMemberIdWithOrder(member.getId()).stream()
                .map(ReservationWaitingResponse::from)
                .collect(Collectors.toList());
        return res;
    }

    public void cancelById(Member member, Long id) {
        ReservationWaitingStatus status = ReservationWaitingStatus.CANCELED;
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(id)
                .orElseThrow(NotExistEntityException::new);

        if (!reservationWaiting.isReservedBy(member)) {
            throw new AuthenticationException();
        }
        reservationWaitingDao.updateStatusById(id, status);
    }
}

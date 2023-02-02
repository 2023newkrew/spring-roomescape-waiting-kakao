package nextstep.reservation;

import auth.AuthenticationException;
import nextstep.exception.NotExistEntityException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservationwaiting.ReservationWaitingDao;
import nextstep.reservationwaiting.ReservationWaitingStatus;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;
    private final MemberDao memberDao;
    private final ReservationWaitingDao reservationWaitingDao;

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao, ReservationWaitingDao reservationWaitingDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
        this.reservationWaitingDao = reservationWaitingDao;
    }

    public Long create(Member member, ReservationRequest reservationRequest) {
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId())
                .orElseThrow(() -> new NotExistEntityException(Schedule.class));

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        Reservation newReservation = new Reservation(
                schedule,
                member,
                ReservationStatus.UNAPPROVED
        );

        return reservationDao.save(newReservation);
    }

    public List<ReservationResponse> findMyReservations(Member member) {
        return ReservationResponse.from(reservationDao.findAllByMemberId(member.getId()));
    }

    public List<ReservationResponse> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId)
                .orElseThrow(() -> new NotExistEntityException(Theme.class));
        return ReservationResponse.from(reservationDao.findAllByThemeIdAndDate(themeId, date));
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new NotExistEntityException(Reservation.class));

        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }

    public void approveReservation(Long reservationId) {
        reservationDao.updateStatusById(reservationId, ReservationStatus.APPROVED);
    }

    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId)
                .orElseThrow(() -> new NotExistEntityException(Reservation.class));
        if (reservation.isUnapproved()) {
            // 예약 취소 확정
            proceedReservationCancellation(reservation);
        }
        if (reservation.isApproved()) {
            // 예약 취소 대기 상태로
            reservationDao.updateStatusById(reservationId, ReservationStatus.WAIT_CANCEL);
        }
    }

    private void proceedReservationCancellation(Reservation reservation) {
        reservationDao.updateStatusById(reservation.getId(), ReservationStatus.CANCELED);
        reservationWaitingDao.updateTop1StatusByStatusAndScheduleId(
                reservation.getSchedule().getId(),
                ReservationWaitingStatus.RESERVED,
                ReservationWaitingStatus.CANCELED
        );
        long waitingSize = reservationWaitingDao.countWaitingByScheduleId(reservation.getSchedule().getId());
        if (waitingSize > 0) {
            reservationWaitingDao.updateTop1StatusByStatusAndScheduleId(
                    reservation.getSchedule().getId(),
                    ReservationWaitingStatus.WAITING,
                    ReservationWaitingStatus.RESERVED);
        }
    }

}

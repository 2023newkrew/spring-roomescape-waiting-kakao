package nextstep.reservation;

import auth.AuthenticationException;
import nextstep.exception.NotExistEntityException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservationwaiting.ReservationWaiting;
import nextstep.reservationwaiting.ReservationWaitingDao;
import nextstep.sales.SalesDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;
    private final MemberDao memberDao;
    private final ReservationWaitingDao reservationWaitingDao;
    private final SalesDao salesDao;

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao, ReservationWaitingDao reservationWaitingDao, SalesDao salesDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
        this.reservationWaitingDao = reservationWaitingDao;
        this.salesDao = salesDao;
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

        long id = reservationDao.save(newReservation);
        return id;
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

    public void approveReservation(Long id) {
        reservationDao.updateStatusById(id, ReservationStatus.APPROVED);
        Reservation reservation = reservationDao.findById(id)
                        .orElseThrow(() -> new NotExistEntityException(Reservation.class));
        salesDao.save(id, reservation.getSchedule().getTheme().getPrice());
    }

    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId)
                .orElseThrow(() -> new NotExistEntityException(Reservation.class));
        if (reservation.isUnapproved()) {
            // 예약 취소 확정
            reservationDao.updateStatusById(reservation.getId(), ReservationStatus.CANCELED);
            updateWaitingsByReservationCancellation(reservation);
        }
        if (reservation.isApproved()) {
            // 예약 취소 대기 상태로
            reservationDao.updateStatusById(reservationId, ReservationStatus.WAIT_CANCEL);
        }
    }

    public void rejectReservation(Long id) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new NotExistEntityException(Reservation.class));
        if (reservation.isUnapproved()) {
            // 미승인 예약 거절
            reservationDao.updateStatusById(reservation.getId(), ReservationStatus.REJECTED);
            updateWaitingsByReservationCancellation(reservation);
        }
        if (reservation.isApproved()) {
            // 승인 예약 거절
            reservationDao.updateStatusById(id, ReservationStatus.WAIT_CANCEL);
            updateWaitingsByReservationCancellation(reservation);
            salesDao.save(id, reservation.getSchedule().getTheme().getPrice() * -1);
        }
    }

    public void approveReservationCancellation(Long id) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new NotExistEntityException(Reservation.class));
        if (!reservation.isWaitCencel()) {
            throw new RuntimeException("예약 취소 대기 상태가 아님");
        }
        reservationDao.updateStatusById(id, ReservationStatus.CANCELED);
        salesDao.save(id, reservation.getSchedule().getTheme().getPrice() * -1);
    }

    private void updateWaitingsByReservationCancellation(Reservation reservation) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findTop1ByScheduleIdOrderByRegTime(reservation.getSchedule().getId()).orElse(null);
        if (reservationWaiting == null) {
            System.out.println("예약 대기가 존재하지 않음");
            return;
        }
        reservationWaitingDao.deleteById(reservationWaiting.getId());
        reservationDao.save(Reservation.from(reservationWaiting));
    }

}

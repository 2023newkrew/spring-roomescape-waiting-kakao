package nextstep.reservation;

import auth.AuthenticationException;
import auth.UserDetails;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import nextstep.waitingreservation.WaitingReservation;
import nextstep.waitingreservation.WaitingReservationDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final WaitingReservationDao waitingReservationDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;
    private final MemberDao memberDao;
    private static final Long LOWEST_WAIT_NUM = 0L;

    public ReservationService(ReservationDao reservationDao, WaitingReservationDao waitingReservationDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.waitingReservationDao = waitingReservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
    }

    public Long create(UserDetails userDetails, ReservationRequest reservationRequest) {
        Member member = memberDao.findById(userDetails.getId());

        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NoSuchElementException("No such schedule exist.");
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());

        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        Reservation newReservation = new Reservation(
                schedule,
                member
        );

        return reservationDao.save(newReservation);
    }

    public List<ReservationResponse> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new NoSuchElementException("No such theme exist.");
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date)
                .stream()
                .map(ReservationResponse::new)
                .collect(Collectors.toList());
    }

    public List<ReservationResponse> findMyReservations(UserDetails userDetails) {
        return reservationDao.findAllByMemberId(userDetails.getId())
                .stream()
                .map(ReservationResponse::new)
                .collect(Collectors.toList());
    }

    public void deleteById(UserDetails userDetails, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NoSuchElementException("No such reservation exist.");
        }
        if (!reservation.sameMember(userDetails)) {
            throw new AuthenticationException();
        }
        reservationDao.deleteById(id);

        Optional<WaitingReservation> firstWaitingReservation = waitingReservationDao.findByScheduleIdAndWaitNum(
                reservation.getSchedule().getId(),
                LOWEST_WAIT_NUM
        );
        firstWaitingReservation.ifPresent(this::moveWaitingReservationToReservation);
    }

    private void moveWaitingReservationToReservation(WaitingReservation waitingReservation) {
        waitingReservationDao.deleteById(waitingReservation.getId());
        waitingReservationDao.adjustWaitNumByScheduleIdAndBaseNum(waitingReservation.getSchedule().getId(), LOWEST_WAIT_NUM);
        Reservation reservation = new Reservation(waitingReservation.getSchedule(), waitingReservation.getMember());
        reservationDao.save(reservation);
    }
}

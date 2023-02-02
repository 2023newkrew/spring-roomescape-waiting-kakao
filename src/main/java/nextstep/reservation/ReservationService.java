package nextstep.reservation;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.support.NotExistEntityException;
import nextstep.theme.ThemeDao;
import nextstep.theme.ThemeService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;
    public final ThemeService themeService;

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao, ThemeService themeService) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
        this.themeService = themeService;
    }

    public Long reserve(Member member, ReservationRequest reservationRequest) {
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId())
                .orElseThrow(NotExistEntityException::new);

        checkIsNotExistByScheduleId(schedule.getId());

        Reservation newReservation = new Reservation(schedule, member);
        return reservationDao.save(newReservation);
    }

    public List<ReservationResponse> findMemberReservations(Member member) {
        return reservationDao.findAllByMemberId(member.getId()).stream()
                .map(ReservationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ReservationResponse> findAllByThemeIdAndDate(Long themeId, Date date) {
        themeService.checkIsExist(themeId);
        return reservationDao.findAllByThemeIdAndDate(themeId, date).stream()
                .map(ReservationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(NotExistEntityException::new);

        if (!reservation.isReservedBy(member)) {
            throw new AuthenticationException();
        }
        if (!reservationDao.deleteById(id)) {
            throw new NotExistEntityException();
        };
    }

    public void checkIsNotExistByScheduleId(Long scheduleId) {
        if (reservationDao.findByScheduleId(scheduleId).isPresent()) {
            throw new DuplicateEntityException();
        }
    }
}

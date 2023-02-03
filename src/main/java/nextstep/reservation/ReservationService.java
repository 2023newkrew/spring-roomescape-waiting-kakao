package nextstep.reservation;

import static nextstep.utils.Validator.checkFieldIsNull;

import java.util.List;
import java.util.Objects;
import nextstep.exception.DuplicateEntityException;
import nextstep.exception.MemberAuthenticationException;
import nextstep.exception.ReservationAuthorizationWebException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao,
                              MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
    }

    public Long create(Member member, ReservationRequest reservationRequest) {
        checkValid(member);
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        checkFieldIsNull(schedule, "schedule");
        if (isDuplicateByScheduleId(reservationRequest.getScheduleId())) {
            throw new DuplicateEntityException(schedule.getId().toString(), "중복되는 예약이 존재합니다.",
                    ReservationService.class.getSimpleName());
        }
        Reservation newReservation = Reservation.builder()
                .member(member)
                .schedule(schedule).build();
        return reservationDao.save(newReservation);
    }

    public boolean isDuplicateByScheduleId(Long scheduleId) {
        return !reservationDao.findByScheduleId(scheduleId).isEmpty();
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        checkFieldIsNull(theme, "theme");
        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Member member, Long id) {
        checkValid(member);
        Reservation reservation = reservationDao.findById(id);
        checkFieldIsNull(reservation, "reservation");
        if (!reservation.sameMember(member)) {
            throw new MemberAuthenticationException("비밀번호가 일치해야 합니다.", member.getPassword(), "delete by id",
                    ReservationService.class.getSimpleName());
        }
        reservationDao.deleteById(id);
    }

    private void checkValid(Member member) {
        if (Objects.isNull(member)) {
            throw new ReservationAuthorizationWebException("해당 권한이 존재해야 하니다.", "member is null", "check valid",
                    ReservationController.class.getSimpleName());
        }
    }
}

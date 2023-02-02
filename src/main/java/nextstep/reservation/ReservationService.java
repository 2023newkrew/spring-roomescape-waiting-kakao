package nextstep.reservation;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.member.MemberService;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleService;
import nextstep.support.DuplicateEntityException;
import nextstep.support.NotExistEntityException;
import nextstep.theme.ThemeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    public final ReservationDao reservationDao;
    public final ThemeService themeService;
    public final ScheduleService scheduleService;
    public final MemberService memberService;

    public ReservationService(ReservationDao reservationDao, ThemeService themeService, ScheduleService scheduleService, MemberService memberService) {
        this.reservationDao = reservationDao;
        this.themeService = themeService;
        this.scheduleService = scheduleService;
        this.memberService = memberService;
    }

    public Long create(Member member, ReservationRequest reservationRequest) {
        if (Objects.isNull(member)) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleService.findById(reservationRequest.getScheduleId()).orElseThrow(NotExistEntityException::new);

        List<Reservation> reservation = reservationDao.findAllByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        Reservation newReservation = new Reservation(
                schedule,
                member
        );

        return reservationDao.save(newReservation);
    }

    public boolean isReserved(Long scheduleId) {
        List<Reservation> reservation = reservationDao.findAllByScheduleId(scheduleId);
        return !reservation.isEmpty();
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        List<Schedule> schedules = scheduleService.findAllByThemeIdAndDate(themeId, date);
        List<Reservation> reservations = new ArrayList<>();

        for (Schedule schedule: schedules) {
            reservationDao
                    .findAllByScheduleId(schedule.getId()).stream()
                    .findFirst()
                    .ifPresent(reservation -> reservations.add(Reservation
                            .builder()
                                    .id(reservation.getId())
                                    .schedule(schedule)
                                    .member(memberService.findById(reservation.getMember().getId()).orElseThrow(NotExistEntityException::new))
                            .build()
                            )
                    );
        }

        return reservations;
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id).orElseThrow(NotExistEntityException::new);

        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAllByMemberId(Member member) {
        return reservationDao.findAllByMemberId(member.getId()).stream()
                .map(ReservationResponse::new)
                .collect(Collectors.toList());
    }
}

package nextstep.reservation;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.member.MemberService;
import nextstep.revenue.Revenue;
import nextstep.revenue.RevenueService;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleService;
import nextstep.support.DuplicateEntityException;
import nextstep.support.NotExistEntityException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ScheduleService scheduleService;
    private final MemberService memberService;
    private final RevenueService revenueService;

    public ReservationService(final ReservationDao reservationDao, final ScheduleService scheduleService, final MemberService memberService, final RevenueService revenueService) {
        this.reservationDao = reservationDao;
        this.scheduleService = scheduleService;
        this.memberService = memberService;
        this.revenueService = revenueService;
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
                member,
                ReservationState.NOT_APPROVED
        );

        return reservationDao.save(newReservation);
    }

    public boolean isReserved(Long scheduleId) {
        List<Reservation> reservation = reservationDao.findAllByScheduleId(scheduleId);
        return !reservation.isEmpty();
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id).orElseThrow(NotExistEntityException::new);

        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }

    public Optional<Reservation> findById(Long id) {
        Reservation reservation = reservationDao.findById(id).orElseThrow(NotExistEntityException::new);
        return Optional.of(Reservation.builder()
                .id(id)
                .schedule(scheduleService.findById(reservation.getSchedule().getId()).orElseThrow(NotExistEntityException::new))
                .member(memberService.findById(reservation.getMember().getId()).orElseThrow(NotExistEntityException::new))
                .state(reservation.getState())
                .build());
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

    public List<ReservationResponse> findAllByMemberId(Member member) {
        return reservationDao.findAllByMemberId(member.getId()).stream()
                .map(ReservationResponse::new)
                .collect(Collectors.toList());
    }

    public void approveReservation(final Long id) {
        reservationDao.updateState(id, ReservationState.APPROVED);
        Reservation reservation = findById(id).orElseThrow(NotExistEntityException::new);

        revenueService.create(Revenue.builder()
                .reservationId(reservation.getId())
                .price(reservation.getSchedule().getTheme().getPrice())
                .build());
    }

    public void cancelReservation(final Long id) {
        Reservation reservation = findById(id).orElseThrow(NotExistEntityException::new);
        if (reservation.getState() == ReservationState.NOT_APPROVED) {
            reservationDao.updateState(id, ReservationState.CANCELED);
        }
        else {
            reservationDao.updateState(id, ReservationState.CANCEL_WAITING);
        }
    }

    public void cancelApproveReservation(final Long id) {
        reservationDao.updateState(id, ReservationState.CANCELED);
        revenueService.deleteByReservationId(id);
    }

    public void rejectReservation(final Long id) {
        Reservation reservation = reservationDao.findById(id).orElseThrow(NotExistEntityException::new);
        if (reservation.getState() == ReservationState.NOT_APPROVED) {

        }
        else {

        }
        reservationDao.updateState(id, ReservationState.REJECT);
    }
}

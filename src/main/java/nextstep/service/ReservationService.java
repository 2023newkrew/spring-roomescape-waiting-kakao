package nextstep.service;

import nextstep.domain.member.Member;
import nextstep.domain.reservation.Reservation;
import nextstep.domain.reservation.ReservationDao;
import nextstep.dto.request.ReservationRequest;
import nextstep.dto.response.ReservationResponse;
import nextstep.domain.schedule.Schedule;
import nextstep.error.ApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.error.ErrorType.*;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationWaitingService reservationWaitingService;
    private final MemberService memberService;
    private final ScheduleService scheduleService;
    private final ThemeService themeService;

    public ReservationService(ReservationDao reservationDao, ReservationWaitingService reservationWaitingService, MemberService memberService, ScheduleService scheduleService, ThemeService themeService) {
        this.reservationDao = reservationDao;
        this.reservationWaitingService = reservationWaitingService;
        this.memberService = memberService;
        this.scheduleService = scheduleService;
        this.themeService = themeService;
    }

    @Transactional
    public Long createReservationOrReservationWaiting(Long memberId, ReservationRequest reservationRequest) {
        Member member = memberService.findById(memberId);
        Schedule schedule = scheduleService.findById(reservationRequest.getScheduleId());

        if (existsByScheduleId(reservationRequest.getScheduleId())) {
            return reservationWaitingService.createReservationWaiting(member, schedule);
        }

        return reservationDao.save(new Reservation(schedule, member));
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        themeService.findById(themeId);
        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    @Transactional(readOnly = true)
    public Boolean existsByScheduleId(Long scheduleId) {
        return reservationDao.existsByScheduleId(scheduleId);
    }

    @Transactional
    public void deleteById(Long memberId, Long reservationId) {
        Member member = memberService.findById(memberId);
        Reservation reservation = findById(reservationId);

        if (!reservation.sameMember(member)) {
            throw new ApplicationException(UNAUTHORIZED_ERROR);
        }

        reservationDao.deleteById(reservationId);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findMyReservations(Long memberId) {
        return reservationDao.findByMemberId(memberId)
                .stream()
                .map(ReservationResponse::new)
                .collect(Collectors.toList());
    }

    private Reservation findById(Long reservationId) {
        return reservationDao.findById(reservationId)
                .orElseThrow(() -> new ApplicationException(RESERVATION_NOT_FOUND));
    }
}

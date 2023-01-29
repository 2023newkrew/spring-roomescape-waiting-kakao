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

    public final ReservationDao reservationDao;
    public final MemberService memberService;
    public final ScheduleService scheduleService;
    public final ThemeService themeService;

    public ReservationService(ReservationDao reservationDao, MemberService memberService, ScheduleService scheduleService, ThemeService themeService) {
        this.reservationDao = reservationDao;
        this.memberService = memberService;
        this.scheduleService = scheduleService;
        this.themeService = themeService;
    }

    @Transactional
    public Long create(Long memberId, ReservationRequest reservationRequest) {
        Member member = memberService.findById(memberId);
        Schedule schedule = scheduleService.findById(reservationRequest.getScheduleId());
        checkIfExistsByScheduleId(reservationRequest.getScheduleId());

        return reservationDao.save(new Reservation(schedule, member));
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        themeService.findById(themeId);
        return reservationDao.findAllByThemeIdAndDate(themeId, date);
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

    @Transactional(readOnly = true)
    public List<Reservation> findByScheduleId(Long scheduleId) {
        return reservationDao.findByScheduleId(scheduleId);
    }

    private Reservation findById(Long reservationId) {
        return reservationDao.findById(reservationId)
                .orElseThrow(() -> new ApplicationException(RESERVATION_NOT_FOUND));
    }

    private void checkIfExistsByScheduleId(Long scheduleId) {
        if (!reservationDao.findByScheduleId(scheduleId).isEmpty()) {
            throw new ApplicationException(DUPLICATE_RESERVATION);
        }
    }
}

package nextstep.service;

import auth.exception.AuthenticationException;
import auth.exception.AuthorizationException;
import nextstep.domain.member.Member;
import nextstep.domain.member.MemberDao;
import nextstep.domain.reservation.Reservation;
import nextstep.domain.reservation.ReservationDao;
import nextstep.domain.reservation.ReservationWaiting;
import nextstep.domain.reservation.ReservationWaitingDao;
import nextstep.domain.schedule.Schedule;
import nextstep.domain.schedule.ScheduleDao;
import nextstep.domain.theme.Theme;
import nextstep.domain.theme.ThemeDao;
import nextstep.dto.request.ReservationRequest;
import nextstep.dto.response.ReservationResponse;
import nextstep.dto.response.ReservationWaitingResponse;
import nextstep.error.ErrorCode;
import nextstep.error.exception.DuplicateEntityException;
import nextstep.error.exception.EntityNotFoundException;
import nextstep.error.exception.NoReservationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    public final ReservationDao reservationDao;
    public final ReservationWaitingDao reservationWaitingDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, ReservationWaitingDao reservationWaitingDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.reservationWaitingDao = reservationWaitingDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
    }

    @Transactional
    public Long create(Long memberId, ReservationRequest reservationRequest) {
        if (memberId == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new EntityNotFoundException(ErrorCode.SCHEDULE_NOT_FOUND);
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException(ErrorCode.DUPLICATE_RESERVATION);
        }

        Reservation newReservation = new Reservation(
                schedule,
                memberDao.findById(memberId)
        );

        return reservationDao.save(newReservation);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new EntityNotFoundException(ErrorCode.THEME_NOT_FOUND);
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    @Transactional
    public void deleteById(Long memberId, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new EntityNotFoundException(ErrorCode.RESERVATION_NOT_FOUND);
        }

        if (!reservation.sameMember(memberId)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }

    @Transactional
    public Long createReservationWaiting(Long memberId, ReservationRequest reservationRequest) {
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());

        if (reservationDao.findByScheduleId(reservationRequest.getScheduleId()).isEmpty()) {
            throw new NoReservationException();
        }

        Member member = memberDao.findById(memberId);

        return reservationWaitingDao.save(new ReservationWaiting(member, schedule));
    }

    @Transactional
    public void deleteReservationWaitingById(Long memberId, Long reservationWaitingId) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(reservationWaitingId);
        if (reservationWaiting == null) {
            throw new EntityNotFoundException(ErrorCode.WAITING_NOT_FOUND);
        }
        if (!reservationWaiting.sameMember(memberId)) {
            throw new AuthorizationException();
        }

        reservationWaitingDao.deleteById(reservationWaitingId);
    }

    @Transactional(readOnly = true)
    public List<ReservationWaitingResponse> findMyReservationWaitings(Long memberId) {
        return reservationWaitingDao.findReservationWaitingsByMemberId(memberId)
                .stream()
                .map(ReservationWaitingResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findMyReservations(Long memberId) {
        return reservationDao.findByMemberId(memberId)
                .stream()
                .map(ReservationResponse::new)
                .collect(Collectors.toList());
    }
}

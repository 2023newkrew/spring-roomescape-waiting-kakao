package nextstep.reservation;

import auth.AuthenticationException;
import nextstep.exceptions.exception.CancelReservationStateException;
import nextstep.exceptions.exception.DuplicatedReservationException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.ThemeDao;
import nextstep.exceptions.exception.NotFoundObjectException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
    }

    public Long create(Member member, ReservationRequest reservationRequest) {
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId())
                .orElseThrow(NotFoundObjectException::new);

        reservationDao.findByScheduleId(schedule.getId())
                .ifPresent((reservation) ->
                        {throw new DuplicatedReservationException();}
                );
        return reservationDao.save(new Reservation(schedule, member));
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        themeDao.findById(themeId).orElseThrow(NullPointerException::new);
        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id).orElseThrow(NullPointerException::new);

        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAllByMember(Member member) {
        List<Reservation> reservations = reservationDao.findAllByMemberId(member.getId());
        return reservations.stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public ReservationResponse cancelReservationFromMember(Member member, Long id) {
        if (member == null) {  // Todo : member Null check는 Intercepter로 이동
            throw new AuthenticationException();
        }
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(NullPointerException::new);
        ReservationState state = getCancelReservationState(reservation);
        reservationDao.updateState(id, state);
        return new ReservationResponse(reservation, state);
    }
    
    public ReservationResponse cancelReservationFromAdmin(Long id) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(NullPointerException::new);
        if (!reservation.getState().equals(ReservationState.CANCEL_WAIT)) {
            throw new CancelReservationStateException("예약 취소 대기상태가 아닙니다.");
        }
        reservationDao.updateState(id, ReservationState.CANCEL);
        return new ReservationResponse(reservation, ReservationState.CANCEL);
    }

    private ReservationState getCancelReservationState(Reservation reservation) {
        if (reservation.getState().equals(ReservationState.UN_APPROVE)) {
            return ReservationState.CANCEL;
        }
        if (reservation.getState().equals(ReservationState.APPROVE)) {
            return ReservationState.CANCEL_WAIT;
        }
        throw new CancelReservationStateException();
    }
}

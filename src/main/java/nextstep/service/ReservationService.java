package nextstep.service;

import auth.support.AuthenticationException;
import nextstep.domain.member.Member;
import nextstep.domain.member.MemberDao;
import nextstep.domain.reservation.Reservation;
import nextstep.domain.reservation.ReservationDao;
import nextstep.domain.reservationwaiting.ReservationWaiting;
import nextstep.domain.reservationwaiting.ReservationWaitingDao;
import nextstep.dto.request.ReservationRequest;
import nextstep.dto.response.ReservationResponse;
import nextstep.dto.response.ReservationWaitingResponse;
import nextstep.domain.schedule.Schedule;
import nextstep.domain.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.domain.theme.Theme;
import nextstep.domain.theme.ThemeDao;
import org.springframework.stereotype.Service;

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

    public Long create(Long memberId, ReservationRequest reservationRequest) {
        Member member = memberDao.findById(memberId);
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
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

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new NullPointerException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Long memberId, Long reservationId) {
        Member member = memberDao.findById(memberId);
        Reservation reservation = reservationDao.findById(reservationId);

        if (reservation == null) {
            throw new NullPointerException();
        }

        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(reservationId);
    }

    public Long createReservationWaiting(Long memberId, ReservationRequest reservationRequest) {
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());

        if (!reservationDao.findByScheduleId(reservationRequest.getScheduleId()).isEmpty()) {
            Member member = memberDao.findById(memberId);
            int waitNum = reservationWaitingDao.findMaxWaitNum(reservationRequest.getScheduleId()) + 1;
            return reservationWaitingDao.save(new ReservationWaiting(member, schedule, waitNum));
        }

        return create(memberId, reservationRequest);
    }

    public void deleteReservationWaitingById(Long memberId, Long reservationWaitingId){
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(reservationWaitingId);
        if(reservationWaiting == null) {
            throw new NullPointerException();
        }
        if(!reservationWaiting.sameMember(memberId)) {
            throw new AuthenticationException();
        }

        reservationWaitingDao.deleteById(reservationWaitingId);
    }

    public List<ReservationWaitingResponse> findMyReservationWaitings(Long memberId) {
        return reservationWaitingDao.findReservationWaitingsByMemberId(memberId)
                .stream()
                .map(ReservationWaitingResponse::new)
                .collect(Collectors.toList());
    }


    public List<ReservationResponse> findMyReservations(Long memberId) {
        return reservationDao.findByMemberId(memberId)
                .stream()
                .map(ReservationResponse::new)
                .collect(Collectors.toList());
    }
}

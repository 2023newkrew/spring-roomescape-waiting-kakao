package nextstep.reservationwaiting.service;

import auth.support.AuthenticationException;
import nextstep.member.domain.Member;
import nextstep.member.repository.MemberDao;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.repository.ReservationDao;
import nextstep.reservationwaiting.domain.ReservationWaiting;
import nextstep.reservationwaiting.dto.ReservationWaitingRequest;
import nextstep.reservationwaiting.dto.ReservationWaitingResponse;
import nextstep.reservationwaiting.repository.ReservationWaitingDao;
import nextstep.schedule.domain.Schedule;
import nextstep.schedule.repository.ScheduleDao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationWaitingService {
    public final ReservationWaitingDao reservationWaitingDao;
    public final ScheduleDao scheduleDao;
    private final ReservationDao reservationDao;
    private final MemberDao memberDao;

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao, ScheduleDao scheduleDao, ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationWaitingDao = reservationWaitingDao;
        this.scheduleDao = scheduleDao;
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    public String create(Long memberId, ReservationWaitingRequest reservationWaitingRequest) {
        Member member = memberDao.findById(memberId);
        if (member == null) {
            throw new AuthenticationException();
        }

        Schedule schedule = scheduleDao.findById(reservationWaitingRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<Reservation> reservations = reservationDao.findByScheduleId(schedule.getId());
        if (reservations.isEmpty()) {
            Reservation reservation = new Reservation(schedule, member);
            return "/reservations/" + reservationDao.save(reservation);
        }

        ReservationWaiting newReservationWaiting = new ReservationWaiting(
                schedule,
                member
        );

        return "/reservation-waitings/" + reservationWaitingDao.save(newReservationWaiting);
    }

    public List<ReservationWaitingResponse> findAllByMemberId(Long memberId) {
        Member member = memberDao.findById(memberId);
        if (member == null) {
            throw new AuthenticationException();
        }

        List<ReservationWaiting> reservationWaitings = reservationWaitingDao.findAllByMemberId(memberId);
        if (reservationWaitings.isEmpty()) {
            return new ArrayList<ReservationWaitingResponse>();
        }

        return reservationWaitings.stream()
                .map(v -> ReservationWaitingResponse.of(v, reservationWaitingDao.getWaitNum(v.getSchedule().getId(), v.getId())))
                .collect(Collectors.toList());
    }

    public void deleteById(Member member, Long id) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(id);
        if (reservationWaiting == null) {
            throw new NullPointerException();
        }

        if (!reservationWaiting.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationWaitingDao.deleteById(id);
    }
}

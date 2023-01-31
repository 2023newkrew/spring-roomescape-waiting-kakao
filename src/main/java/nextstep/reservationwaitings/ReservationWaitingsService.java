package nextstep.reservationwaitings;

import auth.UserDetails;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.NotCreatorMemberException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationWaitingsService {

    private final ReservationWaitingsDao reservationWaitingsDao;
    private final ScheduleDao scheduleDao;
    private final ReservationDao reservationDao;
    private final MemberDao memberDao;

    public ReservationWaitingsService(ReservationWaitingsDao reservationWaitingsDao, ScheduleDao scheduleDao, ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationWaitingsDao = reservationWaitingsDao;
        this.scheduleDao = scheduleDao;
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    public ReservationResult create(UserDetails userDetails, ReservationWaitingRequest request) {
        Schedule schedule = scheduleDao.findById(request.getScheduleId());
        long reservationSize = reservationDao.findByScheduleId(request.getScheduleId()).size();
        Member member = memberDao.findById(userDetails.getId());
        if (reservationSize == 0) {
            Reservation reservation = new Reservation(schedule, member);
            long id = reservationDao.save(reservation);
            return ReservationResult.createReservation(id);
        }
        long waitNum = reservationWaitingsDao.findWaitNumByScheduleId(schedule.getId());
        ReservationWaitings reservationWaitings = new ReservationWaitings(member, schedule, waitNum + 1);
        long id = reservationWaitingsDao.create(reservationWaitings);
        return ReservationResult.createReservationWaiting(id);
    }

    public List<ReservationWaitings> findMyReservationWaitings(UserDetails member) {
        return reservationWaitingsDao.findMyReservationWaitings(member.getId());
    }

    public void delete(UserDetails member, Long id) {
        ReservationWaitings reservationWaitings = reservationWaitingsDao.findById(id);
        if (!reservationWaitings.getMember().getId().equals(member.getId())) {
            throw new NotCreatorMemberException();
        }
        reservationWaitingsDao.delete(id);
    }
}

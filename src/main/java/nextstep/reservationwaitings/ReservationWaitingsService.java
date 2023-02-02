package nextstep.reservationwaitings;

import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.exception.NotCreatorMemberException;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReservationWaitingsService {

    private final ReservationWaitingsDao reservationWaitingsDao;
    private final ReservationWaitingNumGenerator reservationWaitingNumGenerator;
    private final ScheduleDao scheduleDao;
    private final ReservationDao reservationDao;

    public ReservationWaitingsService(ReservationWaitingsDao reservationWaitingsDao,
                                      ReservationWaitingNumGenerator reservationWaitingNumGenerator,
                                      ScheduleDao scheduleDao,
                                      ReservationDao reservationDao) {
        this.reservationWaitingsDao = reservationWaitingsDao;
        this.reservationWaitingNumGenerator = reservationWaitingNumGenerator;
        this.scheduleDao = scheduleDao;
        this.reservationDao = reservationDao;
    }

    public ReservationResult create(Member member, ReservationWaitingRequest request) {
        Schedule schedule = scheduleDao.findById(request.getScheduleId());

        if (isEmptyAtSchedule(request.getScheduleId())) {
            return createReservation(member, schedule);
        }

        return createReservationWaiting(member, schedule);
    }

    private boolean isEmptyAtSchedule(long scheduleId) {
        return reservationDao.findByScheduleId(scheduleId).size() == 0;
    }

    private ReservationResult createReservation(Member member, Schedule schedule) {
        Reservation reservation = new Reservation(schedule, member);
        long id = reservationDao.save(reservation);
        return ReservationResult.createReservation(id);
    }

    private ReservationResult createReservationWaiting(Member member, Schedule schedule) {
        long waitNum = reservationWaitingNumGenerator.getWaitNum(schedule.getId());
        ReservationWaitings reservationWaitings = new ReservationWaitings(member, schedule, waitNum);
        long id = reservationWaitingsDao.create(reservationWaitings);
        return ReservationResult.createReservationWaiting(id);
    }

    public List<ReservationWaitings> findMyReservationWaitings(Member member) {
        return reservationWaitingsDao.findMyReservationWaitings(member.getId());
    }

    public void delete(Member member, Long id) {
        ReservationWaitings reservationWaitings = reservationWaitingsDao.findById(id);
        if (reservationWaitings.getMember().getId() != member.getId()) {
            throw new NotCreatorMemberException();
        }
        reservationWaitingsDao.delete(id);
    }
}

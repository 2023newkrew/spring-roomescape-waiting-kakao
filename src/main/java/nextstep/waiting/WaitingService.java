package nextstep.waiting;

import auth.ForbiddenException;
import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WaitingService {

    private final WaitingDao waitingDao;
    private final ReservationDao reservationDao;
    private final ScheduleDao scheduleDao;

    public WaitingService(WaitingDao waitingDao, ReservationDao reservationDao, ScheduleDao scheduleDao) {
        this.waitingDao = waitingDao;
        this.reservationDao = reservationDao;
        this.scheduleDao = scheduleDao;
    }

    public CreateWaitingResponse create(Member member, CreateWaitingRequest createWaitingRequest) {
        Schedule schedule = scheduleDao.findById(createWaitingRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (reservation.isEmpty()) {
            Reservation savedReservation = reservationDao.save(new Reservation(schedule, member));
            return CreateWaitingResponse.fromReservation(savedReservation);
        }

        Waiting savedWaiting = waitingDao.save(new Waiting(schedule, member));
        return CreateWaitingResponse.fromWaiting(savedWaiting);
    }

    public List<MyWaitingResponse> findAllWaitingsByMemberId(Long id) {
        List<Waiting> myWaitings = waitingDao.findAllByMemberId(id);

        return myWaitings.stream()
                .map(MyWaitingResponse::from)
                .collect(Collectors.toList());
    }

    public void deleteById(Member member, Long id) {
        Waiting waiting = waitingDao.findById(id);
        if (!waiting.sameMember(member)) {
            throw new ForbiddenException();
        }

        waitingDao.deleteById(id);
    }
}

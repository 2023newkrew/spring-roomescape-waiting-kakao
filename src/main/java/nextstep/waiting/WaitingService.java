package nextstep.waiting;

import auth.AuthenticationException;
import auth.UserDetails;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaitingService {

    private final MemberDao memberDao;
    private final ScheduleDao scheduleDao;
    private final ReservationDao reservationDao;
    private final WaitingDao waitingDao;

    public WaitingService(MemberDao memberDao, ScheduleDao scheduleDao, ReservationDao reservationDao, WaitingDao waitingDao) {
        this.memberDao = memberDao;
        this.scheduleDao = scheduleDao;
        this.reservationDao = reservationDao;
        this.waitingDao = waitingDao;
    }

    public WaitingRegisterStatus waitReservation(UserDetails userDetails, WaitingRequest waitingRequest) {
        Member member = memberDao.findById(userDetails.getId());
        if (member == null) {
            throw new AuthenticationException();
        }

        Schedule schedule = scheduleDao.findById(waitingRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<Reservation> reservations = reservationDao.findByScheduleId(schedule.getId());
        if (reservations.isEmpty()) {
            Long reservationId = reservationDao.save(new Reservation(schedule, member));
            return WaitingRegisterStatus.ofReservation(reservationId);
        }

        Long waitingId = waitingDao.save(new Waiting(schedule, member));
        return WaitingRegisterStatus.ofWaiting(waitingId);
    }

    public List<WaitingResponse> getWaiting(UserDetails userDetails) {
        Member member = memberDao.findById(userDetails.getId());
        if (member == null) {
            throw new AuthenticationException();
        }

        List<Waiting> myWaitings = waitingDao.findByMemberId(member.getId());

        for (Waiting waiting : myWaitings) {
            Long waitNum = waitingDao.getWaitNum(waiting);
            waiting.setWaitNum(waitNum);
        }

        return WaitingResponse.toList(myWaitings);
    }

    public void deleteById(UserDetails userDetails, Long id) {
        Member member = memberDao.findById(userDetails.getId());
        if (member == null) {
            throw new AuthenticationException();
        }

        Waiting waiting = waitingDao.findById(id);
        if (waiting == null){
            throw new NullPointerException();
        }

        if (!waiting.sameMember(member)) {
            throw new AuthenticationException();
        }

        waitingDao.deleteById(id);
    }
}

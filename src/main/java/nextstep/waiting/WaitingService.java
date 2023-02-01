package nextstep.waiting;

import auth.AuthenticationException;
import auth.UserDetails;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.support.NoEntityException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
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

    public WaitingRegisterStatus waitForReservation(UserDetails userDetails, WaitingRequest waitingRequest) {
        Member member = findMember(userDetails);

        Schedule schedule = scheduleDao.findById(waitingRequest.getScheduleId());
        if (schedule == null) {
            throw new NoEntityException();
        }

        List<Reservation> reservations = reservationDao.findByScheduleId(schedule.getId());
        if (reservations.isEmpty()) {
            return makeReservation(member, schedule);
        }
        validateAlreadyReserved(member, reservations);

        List<Waiting> waitings = waitingDao.findByScheduleId(schedule.getId());
        validateAlreadyWaiting(member, waitings);

        return makeWaiting(member, schedule);
    }

    private WaitingRegisterStatus makeReservation(Member member, Schedule schedule) {
        Long reservationId = reservationDao.save(new Reservation(schedule, member));
        return WaitingRegisterStatus.ofReservation(reservationId);
    }

    private static void validateAlreadyReserved(Member member, List<Reservation> reservations) {
        for (Reservation reservation : reservations) {
            if (reservation.sameMember(member)) {
                throw new DuplicateEntityException("중복된 예약이 존재합니다.");
            }
        }
    }

    private static void validateAlreadyWaiting(Member member, List<Waiting> waitings) {
        for (Waiting waiting : waitings) {
            if (waiting.sameMember(member)) {
                throw new DuplicateEntityException("중복된 예약 대기가 존재합니다.");
            }
        }
    }

    private WaitingRegisterStatus makeWaiting(Member member, Schedule schedule) {
        Long waitingId = waitingDao.save(new Waiting(schedule, member));
        return WaitingRegisterStatus.ofWaiting(waitingId);
    }

    private Member findMember(UserDetails userDetails) {
        Member member = memberDao.findById(userDetails.getId());
        if (member == null) {
            throw new AuthenticationException();
        }
        return member;
    }

    public List<WaitingResponse> getWaiting(UserDetails userDetails) {
        Member member = findMember(userDetails);

        List<Waiting> myWaitings = waitingDao.findByMemberId(member.getId());

        for (Waiting waiting : myWaitings) {
            Long waitNum = waitingDao.getWaitNum(waiting);
            waiting.setWaitNum(waitNum);
        }

        return WaitingResponse.toList(myWaitings);
    }

    public void deleteById(UserDetails userDetails, Long id) {
        Member member = findMember(userDetails);

        Waiting waiting = waitingDao.findById(id);
        if (waiting == null){
            throw new NoEntityException();
        }

        if (!waiting.sameMember(member)) {
            throw new AuthenticationException();
        }

        waitingDao.deleteById(id);
    }
}

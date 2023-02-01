package nextstep.waitingreservation;

import auth.AuthenticationException;
import auth.UserDetails;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WaitingReservationService {
    public final WaitingReservationDao waitingReservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public WaitingReservationService(WaitingReservationDao waitingReservationDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao) {
        this.waitingReservationDao = waitingReservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
    }

    public Long create(UserDetails userDetails, WaitingReservationRequest waitingReservationRequest) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        Member member = memberDao.findById(userDetails.getId());

        Schedule schedule = scheduleDao.findById(waitingReservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<WaitingReservation> waitingReservations = waitingReservationDao.findAllByScheduleId(schedule.getId());

        WaitingReservation newWaitingReservation = new WaitingReservation(
                schedule,
                member,
                (long) waitingReservations.size()
        );

        return waitingReservationDao.save(newWaitingReservation);
    }

    public List<WaitingReservationResponse> findMyWaitingReservations(UserDetails userDetails) {
        return waitingReservationDao.findAllByMemberId(userDetails.getId())
                .stream()
                .map(WaitingReservationResponse::new)
                .collect(Collectors.toList());
    }

    public void deleteById(UserDetails userDetails, Long id) {
        WaitingReservation waitingReservation = waitingReservationDao.findById(id);
        if (waitingReservation == null) {
            throw new NullPointerException();
        }

        if (!waitingReservation.sameMember(userDetails)) {
            throw new AuthenticationException();
        }

        waitingReservationDao.deleteById(id);
        waitingReservationDao.adjustWaitNumByScheduleIdAndBaseNum(
                waitingReservation.getSchedule().getId(),
                waitingReservation.getWaitNum()
        );
    }
}

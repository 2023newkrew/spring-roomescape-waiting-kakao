package nextstep.waiting;

import auth.utils.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.NotExistException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WaitingService {
    public final WaitingDao waitingDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public WaitingService(WaitingDao waitingDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao) {
        this.waitingDao = waitingDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
    }

    @Transactional
    public Long create(Member member, WaitingRequest waitingRequest) {
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(waitingRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        // 이미 예약이 되어 있는지 체크 -> 만약에 안돼있다면 Error(예약이 가능합니다.)
        List<Waiting> waitings = waitingDao.findByScheduleId(schedule.getId());
        if (waitings.isEmpty()) {
            throw new NotExistException("예약이 존재하지 않기 때문에 예약 대기가 불가능합니다.");
        }

        Waiting waiting = new Waiting(
                schedule,
                member
        );

        return waitingDao.save(waiting);
    }

    public List<Waiting> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new NullPointerException();
        }

        return waitingDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Member member, Long id) {
        Waiting waiting = waitingDao.findById(id);
        if (waiting == null) {
            throw new NullPointerException();
        }

        if (!waiting.sameMember(member)) {
            throw new AuthenticationException();
        }

        waitingDao.deleteById(id);
    }

    private int getWaitingNumber(Waiting waiting){
        return waitingDao.countWaitingNumber(waiting);
    }

    public List<WaitingResponse> findAllByMemberId(Long id) {
        return changeToResponse(waitingDao.findByMemberId(id));
    }

    private List<WaitingResponse> changeToResponse(List<Waiting> waitings){
        return waitings.stream()
                .map(reservation -> new WaitingResponse(reservation, getWaitingNumber(reservation)))
                .collect(Collectors.toList());
    }
}

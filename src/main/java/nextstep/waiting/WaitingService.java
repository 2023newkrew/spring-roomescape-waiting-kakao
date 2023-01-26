package nextstep.waiting;

import auth.AuthenticationException;
import java.util.List;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

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

    public Long create(Member member, WaitingRequest waitingRequest) {
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(waitingRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<Waiting> waiting = waitingDao.findByScheduleId(schedule.getId());
        if (!waiting.isEmpty()) {
            throw new DuplicateEntityException();
        }

        Waiting newWaiting = new Waiting(
                schedule,
                member
        );

        return waitingDao.save(newWaiting);
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
}

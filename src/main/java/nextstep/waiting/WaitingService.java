package nextstep.waiting;

import auth.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingService {

    public final WaitingDao waitingDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public Long create(Member member, WaitingRequest waitingRequest) {
        if (member == null) {
            throw new AuthenticationException();
        }

        Schedule schedule = scheduleDao.findById(waitingRequest.getScheduleId()).orElseThrow(NullPointerException::new);
        List<Waiting> waitings = waitingDao.findByScheduleId(schedule.getId());
        if (waitings.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        Waiting waiting = Waiting.builder()
                .schedule(schedule)
                .member(member)
                .build();

        return waitingDao.save(waiting);
    }

    public List<Waiting> findAllByThemeIdAndDate(Long themeId, String date) {
        themeDao.findById(themeId).orElseThrow(NullPointerException::new);
        return waitingDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Member member, Long id) {
        Waiting waiting = waitingDao.findById(id).orElseThrow(NullPointerException::new);
        if (!waiting.isCreatedBy(member)) {
            throw new AuthenticationException();
        }
        waitingDao.deleteById(id);
    }

    public List<WaitingResponse> findAllByMemberId(Long id) {
        return changeToResponse(waitingDao.findByMemberId(id));
    }

    private int calculateWaitingNumber(Waiting waiting){
        return waitingDao.countWaitingNumber(waiting);
    }

    private List<WaitingResponse> changeToResponse(List<Waiting> waitings){
        return waitings.stream()
                .map(reservation -> WaitingResponse.from(reservation, calculateWaitingNumber(reservation)))
                .collect(Collectors.toList());
    }
}

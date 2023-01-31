package nextstep.waiting;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import nextstep.exceptions.exception.AuthorizationException;
import nextstep.exceptions.exception.NotExistEntityException;
import nextstep.exceptions.exception.NotLoggedInException;
import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingService {

    private final WaitingDao waitingDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;
    private final ReservationDao reservationDao;

    public Long create(Member member, WaitingRequest waitingRequest) {
        if (member == null) {
            throw new NotLoggedInException();
        }

        Schedule schedule = scheduleDao.findById(waitingRequest.getScheduleId())
                .orElseThrow(NotExistEntityException::new);
        List<Reservation> reservations = reservationDao.findByScheduleId(schedule.getId());

        if (reservations.isEmpty()) {
            throw new IllegalStateException();
        }

        Waiting waiting = Waiting.builder()
                .schedule(schedule)
                .member(member)
                .build();

        return waitingDao.save(waiting);
    }

    public List<Waiting> findAllByThemeIdAndDate(Long themeId, String date) {
        themeDao.findById(themeId).orElseThrow(NotExistEntityException::new);
        return waitingDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Member member, Long id) {
        Waiting waiting = waitingDao.findById(id).orElseThrow(NotExistEntityException::new);
        if (!waiting.isCreatedBy(member)) {
            throw new AuthorizationException();
        }
        waitingDao.deleteById(id);
    }

    public List<WaitingResponse> findAllByMemberId(Long id) {
        return changeToResponse(waitingDao.findByMemberId(id));
    }

    private int calculateWaitingNumber(Waiting waiting) {
        return waitingDao.countWaitingNumber(waiting);
    }

    private List<WaitingResponse> changeToResponse(List<Waiting> waitings) {
        return waitings.stream()
                .map(reservation -> WaitingResponse.from(reservation, calculateWaitingNumber(reservation)))
                .collect(Collectors.toList());
    }
}

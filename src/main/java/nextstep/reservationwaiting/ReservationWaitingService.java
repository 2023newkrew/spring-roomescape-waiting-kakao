package nextstep.reservationwaiting;

import static nextstep.utils.Validator.checkFieldIsNull;

import java.util.List;
import java.util.Objects;
import nextstep.exception.NotFoundException;
import nextstep.exception.ReservationAuthorizationWebException;
import nextstep.member.Member;
import nextstep.reservation.ReservationController;
import nextstep.reservationwaiting.dto.ReservationWaitingRequest;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;

@Service
public class ReservationWaitingService {
    private final ReservationWaitingDao reservationWaitingDao;
    private final ScheduleDao scheduleDao;

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao, ScheduleDao scheduleDao) {
        this.reservationWaitingDao = reservationWaitingDao;
        this.scheduleDao = scheduleDao;
    }

    public Long create(Member member, ReservationWaitingRequest reservationWaitingRequest) {
        checkValid(member);
        Schedule schedule = scheduleDao.findById(reservationWaitingRequest.getScheduleId());
        checkFieldIsNull(schedule, "schedule");
        ReservationWaiting newReservationWaiting = ReservationWaiting.builder()
                .schedule(schedule)
                .memberId(member.getId())
                .waitNum(reservationWaitingDao.findMaxWaitNumByScheduleId(reservationWaitingRequest.getScheduleId()) + 1)
                .build();
        newReservationWaiting.toInProgress();
        return reservationWaitingDao.save(newReservationWaiting);
    }

    public List<ReservationWaiting> findAllByMemberId(Long memberId) {
        return reservationWaitingDao.findByMemberId(memberId);
    }

    public void deleteById(Member member, Long id) {
        if (!reservationWaitingDao.existById(id, member.getId())) {
            throw new NotFoundException("해당 멤버의 id에 해당하는 reservation-waiting이 존재해야 합니다.", id.toString(),
                    "삭제할 대상이 존재하지 않습니다.", ReservationWaitingService.class.getSimpleName());
        }
        reservationWaitingDao.deleteById(id);
    }
    public ReservationWaiting findById(Member member, Long id){
        return reservationWaitingDao.findById(id,member.getId())
                .orElseThrow(() -> new NotFoundException("해당 id의 reservation waitings가 존재해야 합니다.", "null", "find by Id",
                        getClass().getSimpleName()));
    }

    private void checkValid(Member member) {
        if (Objects.isNull(member)) {
            throw new ReservationAuthorizationWebException("해당 권한이 존재해야 하니다.", "member is null", "check valid",
                    ReservationController.class.getSimpleName());
        }
    }

    public int findCurrentWaitNum(Long scheduleId,Long waitNum) {
        return reservationWaitingDao.countFinishedByScheduleIdAndWaitNum(scheduleId, waitNum);
    }

    public void dropById(Member member, Long id) {
        reservationWaitingDao.dropById(member.getId(), id);
    }
}
package nextstep.reservationwaiting;

import lombok.RequiredArgsConstructor;
import nextstep.exception.MemberException;
import nextstep.exception.ReservationWaitingException;
import nextstep.exception.RoomEscapeExceptionCode;
import nextstep.exception.ScheduleException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationWaitingService {

    private final MemberDao memberDao;
    private final ScheduleDao scheduleDao;
    private final ReservationWaitingDao reservationWaitingDao;

    public List<ReservationWaitingResponse> findByMemberId(Long memberId) {
        List<ReservationWaiting> reservationWaitingList = reservationWaitingDao.findByMemberId(memberId);
        return reservationWaitingList.stream()
                .map(reservationWaiting -> ReservationWaitingResponse.from(reservationWaiting,
                        reservationWaitingDao.rankBySceduleId(reservationWaiting.getId(), reservationWaiting.getSchedule().getId())))
                .collect(Collectors.toList());
    }

    public Long create(Long memberId, ReservationWaitingRequest reservationWaitingRequest) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new MemberException(RoomEscapeExceptionCode.MEMBER_NOT_FOUND));
        Schedule schedule = scheduleDao.findById(reservationWaitingRequest.getScheduleId())
                .orElseThrow(() -> new ScheduleException(RoomEscapeExceptionCode.SCHEDULE_NOT_FOUND));

        ReservationWaiting newReservationWaiting = new ReservationWaiting(schedule, member);
        return reservationWaitingDao.save(newReservationWaiting);

    }

    public void deleteById(Long memberId, Long id) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new MemberException(RoomEscapeExceptionCode.MEMBER_NOT_FOUND));
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(id)
                .orElseThrow(() -> new ReservationWaitingException(RoomEscapeExceptionCode.RESERVATION_WAITING_NOT_FOUND));

        if (!reservationWaiting.sameMember(member)) {
            throw new ReservationWaitingException(RoomEscapeExceptionCode.NOT_OWN_RESERVATION_WAITING);
        }
        reservationWaitingDao.deleteById(id);
    }
}

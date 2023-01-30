package nextstep.reservationwaiting;

import auth.AuthenticationException;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DoesNotExistEntityException;
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
                .map(ReservationWaitingResponse::from)
                .collect(Collectors.toList());
    }

    public Long create(Long memberId, ReservationWaitingRequest reservationWaitingRequest) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(AuthenticationException::new);
        Schedule schedule = scheduleDao.findById(reservationWaitingRequest.getScheduleId())
                .orElseThrow(DoesNotExistEntityException::new);

        ReservationWaiting newReservationWaiting = new ReservationWaiting(
                schedule,
                member
        );

        return reservationWaitingDao.save(newReservationWaiting);

    }

    public void deleteById(Long memberId, Long id) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(AuthenticationException::new);
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(id)
                .orElseThrow(DoesNotExistEntityException::new);
        if (!reservationWaiting.sameMember(member)) {
            throw new AuthenticationException();
        }
        reservationWaitingDao.deleteById(id);
    }
}

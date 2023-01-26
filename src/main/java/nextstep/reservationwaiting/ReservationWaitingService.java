package nextstep.reservationwaiting;

import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.ReservationDao;
import nextstep.reservation.ReservationService;
import nextstep.reservation.ReservationValidator;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationWaitingService {

    private final MemberDao memberDao;
    private final ScheduleDao scheduleDao;
    private final ReservationDao reservationDao;
    private final ReservationWaitingDao reservationWaitingDao;
    private final ReservationService reservationService;

    private final ReservationValidator reservationValidator;

    public List<ReservationWaitingResponse> findByMemberId(Long memberId) {
        List<ReservationWaiting> reservationWaitingList = reservationWaitingDao.findByMemberId(memberId);
        return reservationWaitingList.stream()
                .map(ReservationWaitingResponse::from)
                .collect(Collectors.toList());
    }

    public Long create(Long memberId, ReservationWaitingRequest reservationWaitingRequest) {
        Member member = memberDao.findById(memberId);
        Schedule schedule = scheduleDao.findById(reservationWaitingRequest.getScheduleId());

        ReservationWaiting newReservationWaiting = new ReservationWaiting(
                schedule,
                member
        );

        try {
            reservationValidator.validateCreation(newReservationWaiting.toReservation());
        } catch (DuplicateEntityException ignored) {

        }
        return reservationWaitingDao.save(newReservationWaiting);

    }

    public void deleteById(Long memberId, Long id) {
        Member member = memberDao.findById(memberId);
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(id);
        reservationValidator.validateDelete(member, reservationWaiting.toReservation());

        reservationDao.deleteById(id);
    }
}

package nextstep.service;

import nextstep.domain.member.Member;
import nextstep.domain.member.MemberDao;
import nextstep.domain.reservationwaiting.ReservationWaiting;
import nextstep.domain.reservationwaiting.ReservationWaitingDao;
import nextstep.domain.schedule.Schedule;
import nextstep.domain.schedule.ScheduleDao;
import nextstep.dto.request.ReservationRequest;
import nextstep.dto.response.ReservationWaitingResponse;
import nextstep.error.ApplicationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.error.ErrorType.RESERVATION_WAITING_NOT_FOUND;
import static nextstep.error.ErrorType.UNAUTHORIZED_ERROR;

@Service
public class ReservationWaitingService {

    private final MemberDao memberDao;
    private final ScheduleDao scheduleDao;
    private final ReservationWaitingDao reservationWaitingDao;
    private final ReservationService reservationService;

    public ReservationWaitingService(MemberDao memberDao, ScheduleDao scheduleDao, ReservationWaitingDao reservationWaitingDao, ReservationService reservationService) {
        this.memberDao = memberDao;
        this.scheduleDao = scheduleDao;
        this.reservationWaitingDao = reservationWaitingDao;
        this.reservationService = reservationService;
    }

    public Long createReservationWaiting(Long memberId, ReservationRequest reservationRequest) {
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());

        if (!reservationService.findByScheduleId(reservationRequest.getScheduleId()).isEmpty()) {
            Member member = memberDao.findById(memberId);
            int waitNum = reservationWaitingDao.findMaxWaitNum(reservationRequest.getScheduleId()) + 1;
            return reservationWaitingDao.save(new ReservationWaiting(member, schedule, waitNum));
        }

        return reservationService.create(memberId, reservationRequest);
    }

    public void deleteReservationWaitingById(Long memberId, Long reservationWaitingId){
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(reservationWaitingId);
        if(reservationWaiting == null) {
            throw new ApplicationException(RESERVATION_WAITING_NOT_FOUND);
        }
        if(!reservationWaiting.sameMember(memberId)) {
            throw new ApplicationException(UNAUTHORIZED_ERROR);
        }

        reservationWaitingDao.deleteById(reservationWaitingId);
    }

    public List<ReservationWaitingResponse> findMyReservationWaitings(Long memberId) {
        return reservationWaitingDao.findReservationWaitingsByMemberId(memberId)
                .stream()
                .map(ReservationWaitingResponse::new)
                .collect(Collectors.toList());
    }

}

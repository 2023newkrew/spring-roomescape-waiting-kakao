package nextstep.service;

import nextstep.domain.member.Member;
import nextstep.domain.reservationwaiting.ReservationWaiting;
import nextstep.domain.reservationwaiting.ReservationWaitingDao;
import nextstep.domain.schedule.Schedule;
import nextstep.dto.request.ReservationRequest;
import nextstep.dto.response.ReservationWaitingResponse;
import nextstep.error.ApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.error.ErrorType.RESERVATION_WAITING_NOT_FOUND;
import static nextstep.error.ErrorType.UNAUTHORIZED_ERROR;

@Service
public class ReservationWaitingService {

    private final ReservationWaitingDao reservationWaitingDao;
    private final MemberService memberService;
    private final ScheduleService scheduleService;
    private final ReservationService reservationService;

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao, MemberService memberService, ScheduleService scheduleService, ReservationService reservationService) {
        this.reservationWaitingDao = reservationWaitingDao;
        this.memberService = memberService;
        this.scheduleService = scheduleService;
        this.reservationService = reservationService;
    }

    @Transactional
    public Long createReservationWaiting(Long memberId, ReservationRequest reservationRequest) {
        Schedule schedule = scheduleService.findById(reservationRequest.getScheduleId());

        if (!reservationService.findByScheduleId(reservationRequest.getScheduleId()).isEmpty()) {
            Member member = memberService.findById(memberId);
            int waitNum = reservationWaitingDao.findMaxWaitNum(reservationRequest.getScheduleId()) + 1;
            return reservationWaitingDao.save(new ReservationWaiting(member, schedule, waitNum));
        }

        return reservationService.create(memberId, reservationRequest);
    }

    @Transactional
    public void deleteReservationWaitingById(Long memberId, Long reservationWaitingId){
        ReservationWaiting reservationWaiting = findById(reservationWaitingId);

        if(!reservationWaiting.sameMember(memberId)) {
            throw new ApplicationException(UNAUTHORIZED_ERROR);
        }

        reservationWaitingDao.deleteById(reservationWaitingId);
    }

    @Transactional(readOnly = true)
    public List<ReservationWaitingResponse> findMyReservationWaitings(Long memberId) {
        return reservationWaitingDao.findByMemberId(memberId)
                .stream()
                .map(ReservationWaitingResponse::new)
                .collect(Collectors.toList());
    }

    private ReservationWaiting findById(Long reservationWaitingId) {
        return reservationWaitingDao.findById(reservationWaitingId)
                .orElseThrow(() -> new ApplicationException(RESERVATION_WAITING_NOT_FOUND));
    }

}

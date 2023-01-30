package nextstep.service;

import nextstep.domain.member.Member;
import nextstep.domain.reservationwaiting.ReservationWaiting;
import nextstep.domain.reservationwaiting.ReservationWaitingDao;
import nextstep.domain.schedule.Schedule;
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

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao) {
        this.reservationWaitingDao = reservationWaitingDao;
    }

    @Transactional
    public synchronized Long createReservationWaiting(Member member, Schedule schedule) {
        int waitNum = reservationWaitingDao.findMaxWaitNum(schedule.getId()) + 1;
        return reservationWaitingDao.save(new ReservationWaiting(member, schedule, waitNum));
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

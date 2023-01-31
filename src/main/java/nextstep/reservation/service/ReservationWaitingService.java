package nextstep.reservation.service;

import auth.exception.AuthenticationException;
import auth.exception.AuthorizationException;
import lombok.RequiredArgsConstructor;
import nextstep.exception.NotExistEntityException;
import nextstep.member.dao.MemberDao;
import nextstep.reservation.dao.ReservationWaitingDao;
import nextstep.reservation.model.CreateReservation;
import nextstep.reservation.model.CreateReservationWaiting;
import nextstep.reservation.model.ReservationWaiting;
import nextstep.reservation.model.ReservationWaitingResponse;
import nextstep.schedule.dao.ScheduleDao;
import nextstep.schedule.model.Schedule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationWaitingService {
    public final ReservationService reservationService;

    public final ReservationWaitingDao reservationWaitingDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    @Transactional
    public Long create(CreateReservationWaiting createWaiting) {
        scheduleDao.findById(createWaiting.getScheduleId())
                .orElseThrow(NotExistEntityException::new);

        ReservationWaiting waitingEntity = new ReservationWaiting(
                createWaiting.getMemberId(),
                createWaiting.getScheduleId(),
                LocalDateTime.now()
        );

        if (reservationService.isReservedByScheduleId(createWaiting.getScheduleId())) {
            reservationWaitingDao.save(waitingEntity);
        }

        return reservationService.create(
            new CreateReservation(
                    createWaiting.getScheduleId()
                    , createWaiting.getMemberName()
            )
        );
    }

    @Transactional(readOnly = true)
    public List<ReservationWaitingResponse> findByMemberId(Long memberId, Long loginId){
        if(!memberId.equals(loginId)){
            throw new AuthorizationException();
        }

        List<ReservationWaiting> myWaitings = reservationWaitingDao.findByMemberId(memberId);
        return getReservationWaitingResponses(myWaitings);
    }

    private List<ReservationWaitingResponse> getReservationWaitingResponses(List<ReservationWaiting> myWaitings) {
        List<ReservationWaitingResponse> myWaitingResponses = new ArrayList<>();

        for (ReservationWaiting reservationWaiting : myWaitings) {
            Long beforeAppliedTimeCount = reservationWaitingDao.countBeforeAppliedTime(
                    reservationWaiting.getScheduleId()
                    ,reservationWaiting.getAppliedTime()
            );

            Schedule schedule = scheduleDao.findById(reservationWaiting.getScheduleId())
                    .orElseThrow(NotExistEntityException::new);

            myWaitingResponses.add(new ReservationWaitingResponse(
                    reservationWaiting.getId()
                    ,schedule
                    ,beforeAppliedTimeCount + 1)
            );
        }
        return myWaitingResponses;
    }

    @Transactional
    public void deleteById(Long memberId, Long id) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(id)
                .orElseThrow(NotExistEntityException::new);

        if (!reservationWaiting.isAppliedBy(memberId)) {
            throw new AuthenticationException();
        }

        reservationWaitingDao.deleteById(id);
    }
}

package nextstep.reservation_waiting;

import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.support.exception.DuplicateEntityException;
import nextstep.support.exception.NoReservationWaitingException;
import nextstep.support.exception.NotOwnReservationWaitingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationWaitingService {
    private final ReservationWaitingDao reservationWaitingDao;

    public Long create(Reservation reservation, Member member) {
        if (reservationWaitingDao.findByMemberId(member.getId())
                .isPresent()) {
            throw new DuplicateEntityException("예약 대기는 중복될 수 없습니다.");
        }
        return reservationWaitingDao.save(reservation, member);
    }

    public ReservationWaiting findById(Long id) {
        return reservationWaitingDao.findById(id)
                .orElseThrow(NoReservationWaitingException::new);
    }

    public void delete(Long reservationWaitingId) {
        reservationWaitingDao.deleteById(reservationWaitingId);
    }

    public void validateByMember(ReservationWaiting reservationWaiting, Member member) {
        if (!reservationWaiting.sameMember(member)) {
            throw new NotOwnReservationWaitingException("자신의 예약 대기가 아닙니다.");
        }
    }

    public List<ReservationWaiting> findOwn(Member member) {
        return reservationWaitingDao.findAllByMemberId(member.getId());
    }

    public ReservationWaitingResponse convertToReservationWaitingResponse(ReservationWaiting reservationWaiting) {
        Long waitNum = calculateWaitNumber(reservationWaiting);

        return ReservationWaitingResponse.builder()
                .id(reservationWaiting.getId())
                .schedule(reservationWaiting.getSchedule())
                .waitNum(waitNum)
                .build();
    }

    public Long calculateWaitNumber(ReservationWaiting reservationWaiting) {
        return reservationWaitingDao.findAllByScheduleId(reservationWaiting.getSchedule()
                        .getId())
                .stream()
                .filter(rw ->  rw.getId() <= reservationWaiting.getId())
                .count();
    }
}

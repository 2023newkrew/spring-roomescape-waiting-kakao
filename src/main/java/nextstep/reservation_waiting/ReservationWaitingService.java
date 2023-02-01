package nextstep.reservation_waiting;

import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.support.exception.DuplicateReservationWaitingException;
import nextstep.support.exception.NonExistReservationWaitingException;
import nextstep.support.exception.NotOwnReservationWaitingException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class ReservationWaitingService {
    private final ReservationWaitingDao reservationWaitingDao;
    private final ReservationDao reservationDao;
    private final MemberDao memberDao;

    public Long create(Reservation reservation, Member member) {
        reservationWaitingDao.findByMemberId(member.getId())
                .ifPresent(reservationWaiting -> {
                    throw new DuplicateReservationWaitingException();
                });

        ReservationWaiting reservationWaiting = new ReservationWaiting(reservation.getSchedule(), member);
        return reservationWaitingDao.save(reservationWaiting);
    }

    public ReservationWaiting findById(Long id) {
        return reservationWaitingDao.findById(id)
                .orElseThrow(NonExistReservationWaitingException::new);
    }

    public Optional<ReservationWaiting> findFirstByScheduleId(Long scheduleId) {
        return reservationWaitingDao.findAllByScheduleId(scheduleId)
                .stream()
                .findFirst();
    }

    public void delete(Long reservationWaitingId) {
        reservationWaitingDao.deleteById(reservationWaitingId);
    }

    public void validateByMember(ReservationWaiting reservationWaiting, Member member) {
        if (!reservationWaiting.sameMember(member)) {
            throw new NotOwnReservationWaitingException();
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
                .takeWhile(rw -> rw.idEqualOrSmall(reservationWaiting.getId()))
                .count();
    }

    public void confirm(Reservation reservation) {
        findFirstByScheduleId(reservation.getSchedule()
                .getId())
                .map(reservationWaiting -> {
                    delete(reservationWaiting.getId());
                    return memberDao.findById(reservationWaiting.getMember()
                                    .getId())
                            .orElseThrow(NonExistReservationWaitingException::new);
                })
                .ifPresent(createReservationWithWaitingMember(reservation));
    }

    private Consumer<Member> createReservationWithWaitingMember(Reservation reservation) {
        return reservationWaitingMember -> reservationDao.save(Reservation.builder()
                .schedule(reservation.getSchedule())
                .member(reservationWaitingMember)
                .build());
    }
}

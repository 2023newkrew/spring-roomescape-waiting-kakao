package nextstep.reservation_waiting;

import static nextstep.exception.ErrorMessage.DUPLICATED_RESERVATION_WAITING;
import static nextstep.exception.ErrorMessage.NOT_EXIST_RESERVATION_WAITING;
import static nextstep.exception.ErrorMessage.NOT_OWN_RESERVATION_WAITING;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import nextstep.exception.DuplicateEntityException;
import nextstep.exception.NoReservationWaitingException;
import nextstep.exception.NotOwnReservationWaitingException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationWaitingService {

    private final ReservationWaitingDao reservationWaitingDao;
    private final ReservationDao reservationDao;
    private final MemberDao memberDao;

    public Long create(final Reservation reservation, final Member member) {
        if (reservationWaitingDao.findByMemberId(member.getId())
            .isPresent()) {
            throw new DuplicateEntityException(DUPLICATED_RESERVATION_WAITING.getMessage());
        }
        return reservationWaitingDao.save(reservation, member);
    }

    public ReservationWaiting findById(final Long id) {
        return reservationWaitingDao.findById(id)
            .orElseThrow(() -> new NoReservationWaitingException(NOT_EXIST_RESERVATION_WAITING.getMessage()));
    }

    public Optional<ReservationWaiting> findByScheduleId(final Long scheduleId) {
        return reservationWaitingDao.findAllByScheduleId(scheduleId)
            .stream()
            .findFirst();
    }

    public void delete(final Long reservationWaitingId) {
        reservationWaitingDao.deleteById(reservationWaitingId);
    }

    public void validateByMember(final ReservationWaiting reservationWaiting, final Member member) {
        if (!reservationWaiting.sameMember(member)) {
            throw new NotOwnReservationWaitingException(NOT_OWN_RESERVATION_WAITING.getMessage());
        }
    }

    public List<ReservationWaiting> findOwn(final Member member) {
        return reservationWaitingDao.findAllByMemberId(member.getId());
    }

    public ReservationWaitingResponse convertToReservationWaitingResponse(final ReservationWaiting reservationWaiting) {
        Long waitNum = calculateWaitNumber(reservationWaiting);

        return ReservationWaitingResponse.builder()
            .id(reservationWaiting.getId())
            .schedule(reservationWaiting.getSchedule())
            .waitNum(waitNum)
            .build();
    }

    public Long calculateWaitNumber(final ReservationWaiting reservationWaiting) {
        return reservationWaitingDao.findAllByScheduleId(reservationWaiting.getSchedule()
                .getId())
            .stream()
            .filter(rw -> rw.getId() <= reservationWaiting.getId())
            .count();
    }


    public void confirm(final Reservation reservation) {
        findByScheduleId(reservation.getSchedule()
            .getId())
            .map(reservationWaiting -> {
                delete(reservationWaiting.getId());
                return memberDao.findById(reservationWaiting.getMember()
                    .getId());
            })
            .ifPresent(getMemberConsumer(reservation));
    }

    private Consumer<Member> getMemberConsumer(final Reservation reservation) {
        return reservationWaitingMember -> reservationDao.save(Reservation.builder()
            .schedule(reservation.getSchedule())
            .member(reservationWaitingMember)
            .build());
    }
}

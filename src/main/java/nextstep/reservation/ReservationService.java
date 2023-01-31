package nextstep.reservation;

import lombok.RequiredArgsConstructor;
import nextstep.exception.*;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.ThemeDao;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;
    private final MemberDao memberDao;
    private final ApplicationEventPublisher publisher;

    public Long create(Long memberId, ReservationRequest reservationRequest) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new MemberException(RoomEscapeExceptionCode.MEMBER_NOT_FOUND));
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId())
                .orElseThrow(() -> new ScheduleException(RoomEscapeExceptionCode.SCHEDULE_NOT_FOUND));

        Reservation newReservation = new Reservation(schedule, member);

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new ReservationException(RoomEscapeExceptionCode.RESERVED_SCHEDULE);
        }

        return reservationDao.save(newReservation);
    }

    public List<ReservationResponse> findAllByThemeIdAndDate(Long themeId, String date) {
        themeDao.findById(themeId)
                .orElseThrow(() -> new ThemeException(RoomEscapeExceptionCode.THEME_NOT_FOUND));

        return reservationDao.findAllByThemeIdAndDate(themeId, date)
                .stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }

    public void deleteById(Long memberId, Long id) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new MemberException(RoomEscapeExceptionCode.MEMBER_NOT_FOUND));
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new ReservationException(RoomEscapeExceptionCode.RESERVATION_NOT_FOUND));
        if (!reservation.sameMember(member)) {
            throw new ReservationException(RoomEscapeExceptionCode.NOT_OWN_RESERVATION);
        }

        reservationDao.deleteById(id);

        publisher.publishEvent(new ReservationDeleteEvent(reservation));
    }

    public List<ReservationResponse> findByMemberId(Long memberId) {
        return reservationDao.findByMemberId(memberId)
                .stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }
}

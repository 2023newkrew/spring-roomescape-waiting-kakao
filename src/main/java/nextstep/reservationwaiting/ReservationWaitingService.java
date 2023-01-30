package nextstep.reservationwaiting;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationWaitingService {
    public final ReservationWaitingDao reservationWaitingDao;
    public final ScheduleDao scheduleDao;
    private final ReservationDao reservationDao;
    private final MemberDao memberDao;

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao, ScheduleDao scheduleDao, ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationWaitingDao = reservationWaitingDao;
        this.scheduleDao = scheduleDao;
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    public String create(Long memberId, ReservationWaitingRequest reservationWaitingRequest) {
        Member member = memberDao.findById(memberId);
        if (member == null) {
            throw new AuthenticationException();
        }

        Schedule schedule = scheduleDao.findById(reservationWaitingRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<Reservation> reservations = reservationDao.findByScheduleId(schedule.getId());
        if (reservations.isEmpty()) {
            Reservation reservation = new Reservation(schedule, member);
            return "/reservation/" + reservationDao.save(reservation);
        }

        ReservationWaiting newReservationWaiting = new ReservationWaiting(
                schedule,
                member
        );

        return "/reservation-waiting/" + reservationWaitingDao.save(newReservationWaiting);
    }

    public List<ReservationWaitingResponse> findAllByMemberId(Long memberId) {
        Member member = memberDao.findById(memberId);
        if (member == null) {
            throw new AuthenticationException();
        }

        List<ReservationWaiting> reservationWaitings = reservationWaitingDao.findAllByMemberId(memberId);
        if (reservationWaitings.isEmpty()) {
            System.out.println("ASDASDAD");
            return new ArrayList<ReservationWaitingResponse>();
        }

        return reservationWaitings.stream()
                .map(v -> ReservationWaitingResponse.of(v, reservationWaitingDao.getWaitNum(v.getSchedule().getId(), v.getId())))
                .collect(Collectors.toList());
    }
//
//    public void deleteById(Member member, Long id) {
//        Reservation reservation = reservationDao.findById(id);
//        if (reservation == null) {
//            throw new NullPointerException();
//        }
//
//        if (!reservation.sameMember(member)) {
//            throw new AuthenticationException();
//        }
//
//        reservationDao.deleteById(id);
//    }
}

package nextstep.reservation;

import auth.AuthenticationException;
import auth.UserDetails;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.dto.ReservationCreateDto;
import nextstep.reservation.dto.ReservationDeleteDto;
import nextstep.reservation.dto.ReservationReadDto;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReservationService {
    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public ReservationCreateDto create(Member member, ReservationRequest reservationRequest) {
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        boolean isReserved = true;
        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            isReserved = false;
        }

        Long id = reservationDao.save(new Reservation(schedule, member));
        return new ReservationCreateDto(isReserved, id);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new NullPointerException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public List<Reservation> findReservationsByUserDetails(UserDetails userDetails) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }
        return reservationDao.findByMemberId(userDetails.getId());
    }

    public ReservationDeleteDto cancelReservation(UserDetails userDetails, Long id) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            return new ReservationDeleteDto(0);
        }

        if (!reservation.sameMemberId(userDetails.getId())) {
            throw new AuthenticationException();
        }

        return new ReservationDeleteDto(reservationDao.deleteById(id));
    }

    private boolean isClassified(ReservationState reservationState, Long waitNum) {
        return (reservationState == ReservationState.RESERVED && waitNum == 0) || (
                reservationState == ReservationState.WAITING && waitNum > 0);
    }

    public List<ReservationReadDto> classifyReservations(List<Reservation> reservations,
                                                         ReservationState reservationState) {
        List<ReservationReadDto> reservationReadDtos = new ArrayList<>();
        for (Reservation reservation : reservations) {
            Long waitNum = getWaitNum(reservation);
            if (isClassified(reservationState, waitNum)) {
                reservationReadDtos.add(new ReservationReadDto(
                        reservation.getId(),
                        reservation.getSchedule(),
                        waitNum)
                );
            }
        }
        return reservationReadDtos;
    }

    public Long getWaitNum(Reservation reservation) {
        return reservationDao.calcWaitNumByScheduleId(
                reservation.getSchedule().getId(),
                reservation.getId()
        );
    }

    public ReservationReadDto findReservationById(Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }
        Long waitNum = getWaitNum(reservation);
        return new ReservationReadDto(reservation.getId(), reservation.getSchedule(), waitNum);
    }
}

package nextstep.service;

import auth.domain.UserDetails;
import auth.support.AuthenticationException;
import nextstep.controller.dto.request.ReservationRequest;
import nextstep.controller.dto.response.ReservationResponse;
import nextstep.domain.*;
import nextstep.repository.*;
import nextstep.support.DuplicateEntityException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;
    public final SaleHistoryDao saleHistoryDao;

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao, SaleHistoryDao saleHistoryDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
        this.saleHistoryDao = saleHistoryDao;
    }

    public long create(UserDetails userDetails, ReservationRequest reservationRequest) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new IllegalArgumentException();
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        Reservation newReservation = new Reservation(
                schedule,
                new Member(userDetails),
                ReservationStatus.CREATED
        );

        return reservationDao.save(newReservation);
    }

    public List<Reservation> findAllByThemeIdAndDate(long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new IllegalArgumentException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public List<ReservationResponse> findAllByUserId(long id) {
        return reservationDao.findAllByUserId(id).stream().map(ReservationResponse::new).collect(Collectors.toList());
    }

    public void deleteById(UserDetails userDetails, long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new IllegalArgumentException();
        }

        if (!reservation.sameMember(new Member(userDetails))) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }

    public void approveReservationById(long id) {
        /*
         * 예약 요청(CREATED) → 승인 처리
         */
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new IllegalArgumentException();
        }

        switch (reservation.getReservationStatus()) {
            case CREATED -> {
                reservationDao.updateStatus(id, ReservationStatus.APPROVED);
                saleHistoryDao.save(
                        new SaleHistory(
                                reservation.getSchedule().getTheme().getName(),
                                reservation.getSchedule().getTheme().getPrice(),
                                reservation.getSchedule().getDate(),
                                reservation.getSchedule().getTime(),
                                reservation.getMember().getUsername(),
                                reservation.getMember().getPhone(),
                                id
                        )
                );
            }
        }
    }

    public void cancelById(long id) {
        /*
         * 예약 요청(CREATED) → 취소 처리
         * 승인된 요청(REQUESTED_CANCEL) → 취소 요청 처리
         */
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new IllegalArgumentException();
        }

        switch (reservation.getReservationStatus()) {
            case CREATED -> reservationDao.updateStatus(id, ReservationStatus.CANCELLED);
            case APPROVED -> reservationDao.updateStatus(id, ReservationStatus.REQUESTED_CANCEL);
        }
    }

    public void cancelRequestedById(long id) {
        /*
         * 승인한 예약 취소 요청(REQUESTED_CANCEL) → 역방항 매출 기록 후 취소 처리
         */
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new IllegalArgumentException();
        }

        switch (reservation.getReservationStatus()) {
            /* 승인한 예약 취소 요청 상태 */
            case REQUESTED_CANCEL -> {
                saleHistoryDao.save(
                        new SaleHistory(
                                reservation.getSchedule().getTheme().getName(),
                                reservation.getSchedule().getTheme().getPrice() * -1,
                                reservation.getSchedule().getDate(),
                                reservation.getSchedule().getTime(),
                                reservation.getMember().getUsername(),
                                reservation.getMember().getPhone(),
                                id
                        )
                );
                reservationDao.updateStatus(id, ReservationStatus.CANCELLED);
            }
        }
    }

    public void rejectById(long id) {
        /*
         * 예약 요청(CREATED) → 거절 처리
         * 승인한 예약 취소 요청(APPROVED) → 역방항 매출 기록 후 거절 처리
         */
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new IllegalArgumentException();
        }

        switch (reservation.getReservationStatus()) {
            /* 승인한 예약 취소 요청 상태, 예약 승인 상태 */
            case REQUESTED_CANCEL, APPROVED -> {
                saleHistoryDao.save(
                        new SaleHistory(
                                reservation.getSchedule().getTheme().getName(),
                                reservation.getSchedule().getTheme().getPrice() * -1,
                                reservation.getSchedule().getDate(),
                                reservation.getSchedule().getTime(),
                                reservation.getMember().getUsername(),
                                reservation.getMember().getPhone(),
                                id
                        )
                );
                reservationDao.updateStatus(id, ReservationStatus.REJECTED);
            }
            /* 예약 요청 상태 */
            case CREATED -> {
                reservationDao.updateStatus(id, ReservationStatus.REJECTED);
            }
        }
    }
}

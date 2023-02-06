package nextstep.service;

import lombok.RequiredArgsConstructor;
import nextstep.domain.member.Member;
import nextstep.domain.reservation.Reservation;
import nextstep.domain.reservation.ReservationDao;
import nextstep.domain.reservation.ReservationStatus;
import nextstep.dto.request.ReservationRequest;
import nextstep.dto.response.CreateReservationResponse;
import nextstep.dto.response.ReservationResponse;
import nextstep.domain.schedule.Schedule;
import nextstep.error.ApplicationException;
import nextstep.utils.TransactionUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.domain.reservation.ReservationStatus.*;
import static nextstep.error.ErrorType.*;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final static int DEPOSIT_POLICY = 2000;

    private final ReservationDao reservationDao;
    private final ReservationWaitingService reservationWaitingService;
    private final MemberService memberService;
    private final ScheduleService scheduleService;
    private final ThemeService themeService;
    private final SalesHistoryService salesHistoryService;
    private final TransactionUtil transactionUtil;

    @Transactional
    public CreateReservationResponse createReservationOrReservationWaiting(Long memberId, ReservationRequest reservationRequest) {
        Member member = memberService.findById(memberId);
        Schedule schedule = scheduleService.findById(reservationRequest.getScheduleId());

        if (reservationRequest.isLessThanDepositPolicy(DEPOSIT_POLICY)) {
            throw new ApplicationException(RESERVATION_DEPOSIT_NOT_ENOUGH, DEPOSIT_POLICY);
        }

        if (existsByScheduleId(reservationRequest.getScheduleId())) {
            return new CreateReservationResponse(reservationWaitingService.createReservationWaiting(member, schedule, reservationRequest.getDeposit()), false);
        }

        return new CreateReservationResponse(reservationDao.save(new Reservation(schedule, member, reservationRequest.getDeposit())), true);
    }

    public List<ReservationResponse> findAllByThemeIdAndDate(Long themeId, String date) {
        return transactionUtil.executeReadOnlyTask(() -> {
                    themeService.checkThemeExists(themeId);
                    return reservationDao.findAllByThemeIdAndDate(themeId, date);
                })
                .stream()
                .map(ReservationResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Boolean existsByScheduleId(Long scheduleId) {
        return reservationDao.existsByScheduleId(scheduleId);
    }

    public List<ReservationResponse> findMyReservations(Long memberId) {
        return transactionUtil.executeReadOnlyTask(() -> reservationDao.findByMemberId(memberId))
                .stream()
                .map(ReservationResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void approveReservation(Long reservationId) {
        Reservation reservation = findById(reservationId);
        validateTransition(reservation.getStatus(), UNAPPROVED);

        if (reservation.isLessThanDepositPolicy(DEPOSIT_POLICY)) {
            throw new ApplicationException(RESERVATION_DEPOSIT_NOT_ENOUGH, DEPOSIT_POLICY);
        }

        reservationDao.updateReservationStatus(reservationId, reservation.getTransitionedStatus().name());
        salesHistoryService.savePaymentHistory(reservation);
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = findById(reservationId);
        validateTransition(reservation.getStatus(), UNAPPROVED, APPROVED);

        if (reservation.isUnapproved()) {
            reservationDao.updateReservationStatus(reservationId, CANCELED.name());
            return;
        }

        reservationDao.updateReservationStatus(reservationId, CANCEL_PENDING.name());
    }

    @Transactional
    public void approveCancelReservation(Long reservationId) {
        Reservation reservation = findById(reservationId);
        validateTransition(reservation.getStatus(), CANCEL_PENDING);

        reservationDao.updateReservationStatus(reservationId, CANCELED.name());
        salesHistoryService.saveRefundHistory(reservation);
    }

    @Transactional
    public void rejectReservation(Long reservationId) {
        Reservation reservation = findById(reservationId);
        validateTransition(reservation.getStatus(), UNAPPROVED, APPROVED);

        reservationDao.updateReservationStatus(reservationId, REJECTED.name());
        if (!reservation.isUnapproved()) {
            salesHistoryService.saveRefundHistory(reservation);
        }
    }

    private void validateTransition(ReservationStatus actualStatus, ReservationStatus... expectedStatuses) {
        Arrays.stream(expectedStatuses)
                .filter(status -> status.equals(actualStatus))
                .findAny()
                .orElseThrow(() -> new ApplicationException(INTERNAL_SERVER_ERROR));
    }

    private Reservation findById(Long reservationId) {
        return reservationDao.findById(reservationId)
                .orElseThrow(() -> new ApplicationException(RESERVATION_NOT_FOUND));
    }
}

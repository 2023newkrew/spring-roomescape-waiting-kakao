package nextstep.waiting;

import auth.token.JwtTokenProvider;
import nextstep.AbstractTest;
import nextstep.error.ErrorCode;
import nextstep.exception.DuplicateEntityException;
import nextstep.exception.NotExistEntityException;
import nextstep.exception.UnauthorizedException;
import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.role.Role;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class ReservationWaitingServiceTest extends AbstractTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    ReservationWaitingService reservationWaitingService;
    @Autowired
    ReservationWaitingDao reservationWaitingDao;
    @Autowired
    ReservationDao reservationDao;

    Theme theme;
    Schedule schedule;

    @BeforeEach
    void setUp() {
        theme = createNewTheme();
        schedule = createNewSchedule(theme.getId());
    }

    @Nested
    @DisplayName("예약 대기를 생성하는 create 메소드는")
    class Create {
        @Nested
        @DisplayName("에약 대기 생성을 요청받은 스케줄에 예약과 예약 대기가 없으면")
        class noReservationAndWaitings {
            @Test
            @DisplayName("예약을 생성한다")
            void createReservation() {
                Member member = createNewMember(Role.USER);
                ReservationWaitingRequest emptyScheduleRequest = new ReservationWaitingRequest(schedule.getId());

                Long reservationId = reservationWaitingService.create(member, emptyScheduleRequest);

                Optional<Reservation> reservation = reservationDao.findById(reservationId);

                assertThat(reservation).isNotEmpty();
                assertThat(reservation.get().getMember().getId()).isEqualTo(member.getId());
                assertThat(reservation.get().getScheduleId()).isEqualTo(emptyScheduleRequest.getScheduleId());
            }
        }

        @Nested
        @DisplayName("에약 대기 생성을 요청받은 스케줄에 예약이 있으면")
        class existReservation {
            Member member1;
            Reservation reservation;

            @BeforeEach
            void setUp() {
                member1 = createNewMember(Role.USER);
                reservation = createNewReservation(schedule.getId(), member1);
            }

            @Test
            @DisplayName("예약 대기를 생성한다")
            void createReservationWaiting() {
                Member member2 = createNewMember(Role.USER);
                ReservationWaitingRequest onlyReservationExistScheduleRequest = new ReservationWaitingRequest(schedule.getId());

                Long reservationWaitingId = reservationWaitingService.create(member2, onlyReservationExistScheduleRequest);

                Optional<ReservationWaiting> reservationWaiting = reservationWaitingDao.findById(reservationWaitingId);

                assertThat(reservationWaiting).isNotEmpty();
                assertThat(reservationWaiting.get().getWaitingSeq()).isEqualTo(1);
                assertThat(reservationWaiting.get().getReservation().getMember().getId()).isEqualTo(member2.getId());
                assertThat(reservationWaiting.get().getScheduleId()).isEqualTo(onlyReservationExistScheduleRequest.getScheduleId());
            }
        }

        @Nested
        @DisplayName("예약 대기 생성을 요청받은 스케줄에 예약과 예약 대기가 각각 1개씩 있으면")
        class existReservationAndWaiting {
            Member member1;
            Member member2;

            @BeforeEach
            void setUp() {
                member1 = createNewMember(Role.USER);
                member2 = createNewMember(Role.USER);
                createNewReservation(schedule.getId(), member1);
                createNewReservationWaiting(schedule.getId(), member2);
            }

            @Test
            @DisplayName("waitingSeq가 2인 예약 대기를 생성한다")
            void createReservationWaitingWithWaitingSeq2() {
                Member member3 = createNewMember(Role.USER);
                ReservationWaitingRequest reservationAndWaitingExistSchedule = new ReservationWaitingRequest(schedule.getId());

                Long reservationWaitingId = reservationWaitingService.create(member3, reservationAndWaitingExistSchedule);

                Optional<ReservationWaiting> reservationWaiting = reservationWaitingDao.findById(reservationWaitingId);

                assertThat(reservationWaiting).isNotEmpty();
                assertThat(reservationWaiting.get().getWaitingSeq()).isEqualTo(2L);
                assertThat(reservationWaiting.get().getReservation().getMember().getId()).isEqualTo(member3.getId());
                assertThat(reservationWaiting.get().getScheduleId()).isEqualTo(reservationAndWaitingExistSchedule.getScheduleId());
            }
        }

        @Nested
        @DisplayName("예약 대기 생성을 요청받은 스케줄에 요청을 한 멤버의 예약이 이미 존재하면")
        class HasSameMemberReservation {
            Member member;

            @BeforeEach
            void setUp() {
                member = createNewMember(Role.USER);
                createNewReservation(schedule.getId(), member);
            }

            @Test
            @DisplayName("DUPLICATE_RESERVATION 코드를 담은 DuplicateEntityException 예외를 던진다")
            void throwsDuplicateEntityException() {
                ReservationWaitingRequest reservationWaitingRequest = new ReservationWaitingRequest(schedule.getId());
                assertThatThrownBy(() ->
                        reservationWaitingService.create(member, reservationWaitingRequest))
                        .isInstanceOf(DuplicateEntityException.class)
                        .extracting("errorCode").isEqualTo(ErrorCode.DUPLICATE_RESERVATION);
            }
        }
    }

    @Nested
    @DisplayName("멤버 id로 예약 대기를 조회하는 findByMemberId 메소드는")
    class findByMemberId {

        @Nested
        @DisplayName("멤버 id에 해당하는 멤버가 1개의 예약 대기를 갖고있으면")
        class hasReservationWaitings {
            Member member1;
            Member member2;
            ReservationWaiting reservationWaiting;

            @BeforeEach
            void setUp() {
                member1 = createNewMember(Role.USER);
                member2 = createNewMember(Role.USER);
                createNewReservation(schedule.getId(), member1);
                reservationWaiting = createNewReservationWaiting(schedule.getId(), member2);
            }

            @Test
            @DisplayName("size 1의 예약 대기 List를 반환한다")
            void returnSize1ReservationWaitingList() {
                List<ReservationWaiting> reservationWaitings = reservationWaitingService.findByMemberId(member2);
                assertThat(reservationWaitings)
                        .hasSize(1)
                        .containsExactly(reservationWaiting);
            }
        }

        @Nested
        @DisplayName("멤버 id에 해당하는 멤버가 2개의 예약 대기를 갖고있으면")
        class hasMultipleReservationWaitings {
            Member member1;
            Member member2;
            ReservationWaiting reservationWaiting1;
            ReservationWaiting reservationWaiting2;

            @BeforeEach
            void setUp() {
                Schedule schedule2 = createNewSchedule(theme.getId());

                member1 = createNewMember(Role.USER);
                createNewReservation(schedule.getId(), member1);
                createNewReservation(schedule2.getId(), member1);

                member2 = createNewMember(Role.USER);
                reservationWaiting1 = createNewReservationWaiting(schedule.getId(), member2);
                reservationWaiting2 = createNewReservationWaiting(schedule2.getId(), member2);
            }

            @Test
            @DisplayName("size 2의 예약 대기 List를 반환한다")
            void returnSize1ReservationWaitingList() {
                List<ReservationWaiting> reservationWaitings = reservationWaitingService.findByMemberId(member2);
                assertThat(reservationWaitings)
                        .hasSize(2)
                        .containsExactly(reservationWaiting1, reservationWaiting2);
            }
        }

        @Nested
        @DisplayName("멤버 id에 해당하는 멤버가 예약 대기를 갖고있지 않으면")
        class hasNoReservationWaiting {
            Member member;

            @BeforeEach
            void setUp() {
                member = createNewMember(Role.USER);
            }

            @Test
            @DisplayName("빈 예약 대기 List를 반환한다")
            void returnSize1ReservationWaitingList() {
                List<ReservationWaiting> reservationWaitings = reservationWaitingService.findByMemberId(member);
                assertThat(reservationWaitings).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("예약 대기를 삭제하는 deleteById 메소드는")
    class deleteById {

        @Nested
        @DisplayName("넘겨받은 삭제 요청 예약대기 id에 해당하는 스케줄에 1개의 예약대기만 존재하면")
        class existOnlyOneReservationWaiting {
            Member member2;
            ReservationWaiting reservationWaiting;

            @BeforeEach
            void setUp() {
                Member member1 = createNewMember(Role.USER);
                createNewReservation(schedule.getId(), member1);

                member2 = createNewMember(Role.USER);
                reservationWaiting = createNewReservationWaiting(schedule.getId(), member2);
            }

            @Test
            @DisplayName("id에 해당하는 예약 대기를 삭제한다")
            void justDelete() {
                assertThatNoException().
                        isThrownBy(() -> reservationWaitingService.deleteById(member2, reservationWaiting.getReservationId()));
                assertThat(reservationWaitingDao.findById(reservationWaiting.getReservationId())).isEmpty();
            }
        }

        @Nested
        @DisplayName("넘겨받은 삭제 요청 예약대기 id에 해당하는 스케줄에 여러개의 예약대기가 존재하면")
        class existMultipleReservationWaitings {
            Member member2;
            ReservationWaiting reservationWaiting1;
            ReservationWaiting reservationWaiting2;
            ReservationWaiting reservationWaiting3;

            @BeforeEach
            void setUp() {
                Member member1 = createNewMember(Role.USER);
                createNewReservation(schedule.getId(), member1);

                member2 = createNewMember(Role.USER);
                reservationWaiting1 = createNewReservationWaiting(schedule.getId(), member2);

                Member member3 = createNewMember(Role.USER);
                reservationWaiting2 = createNewReservationWaiting(schedule.getId(), member3);
                Member member4 = createNewMember(Role.USER);
                reservationWaiting3 = createNewReservationWaiting(schedule.getId(), member4);
            }

            @Test
            @DisplayName("id에 해당하는 예약 대기를 삭제하고, 삭제한 예약 대기보다 예약 순번이 큰 예약 대기의 순번을 1씩 감소시킨다")
            void justDelete() {
                assertThatNoException().
                        isThrownBy(() -> reservationWaitingService.deleteById(member2, reservationWaiting1.getReservationId()));
                assertThat(reservationWaitingDao.findById(reservationWaiting1.getReservationId())).isEmpty();

                Optional<ReservationWaiting> result1 = reservationWaitingDao.findById(reservationWaiting2.getReservationId());
                Optional<ReservationWaiting> result2 = reservationWaitingDao.findById(reservationWaiting3.getReservationId());

                assertThat(result1).isNotEmpty();
                assertThat(result1.get().getWaitingSeq()).isEqualTo(1);

                assertThat(result2).isNotEmpty();
                assertThat(result2.get().getWaitingSeq()).isEqualTo(2);
            }
        }

        @Nested
        @DisplayName("삭제 요청 예약대기의 소유자가 아닌 다른 멤버가 삭제 요청을 하면")
        class unauthorizedMember {
            Member member;
            ReservationWaiting reservationWaiting;

            @BeforeEach
            void setUp() {
                Member member1 = createNewMember(Role.USER);
                createNewReservation(schedule.getId(), member1);

                Member member2 = createNewMember(Role.USER);
                reservationWaiting = createNewReservationWaiting(schedule.getId(), member2);

                member = createNewMember(Role.USER);
            }

            @Test
            @DisplayName("ErrorCode.FORBIDDEN을 담은 UnauthorizedException을 발생시킨다")
            void throwUnauthorizedException() {
                assertThatThrownBy(() -> reservationWaitingService.deleteById(member, reservationWaiting.getReservationId()))
                        .isInstanceOf(UnauthorizedException.class)
                        .extracting("errorCode")
                        .isEqualTo(ErrorCode.FORBIDDEN);
            }
        }

        @Nested
        @DisplayName("삭제 요청 예약대기 id에 해당하는 예약 대기가 존재하지 않으면")
        class noReservationWaiting {
            Member member;

            @BeforeEach
            void setUp() {
                member = createNewMember(Role.USER);
            }

            @Test
            @DisplayName("ErrorCode.RESERVATION_NOT_FOUND를 담은 NotExistEntityException을 발생시킨다")
            void throwNotExistEntityException() {
                assertThatThrownBy(() -> reservationWaitingService.deleteById(member, 1L))
                        .isInstanceOf(NotExistEntityException.class)
                        .extracting("errorCode")
                        .isEqualTo(ErrorCode.RESERVATION_NOT_FOUND);
            }
        }
    }
}
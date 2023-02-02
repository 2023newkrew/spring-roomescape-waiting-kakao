# spring-roomesacpe-waiting

### 1단계 프로그래밍 요구사항
- auth 패키지를 nextstep 패키지로부터 분리한다
  - auth 패키지에서 nextstep로 의존하는 부분을 제거한다.
  - auth 패키지 내에서 스프링 빈으로 사용되던 객체를 Component Scan이 아닌 Java Configuration으로 빈 등록한다.

### 1단계 기능 구현 목록
- [x] nextstep 패키지에서 auth 패키지 분리
  - [x] auth 패키지에서 nextstep 패키지로의 의존성 제거
  - [x] auth 패키지 내 스프링 빈을 Java Configuration으로 빈 등록


### 2단계 프로그래밍 요구사항
- 이미 예약이 된 스케줄 대상으로 예약 대기를 신청할 수 있다.
  - 예약이 없는 스케줄에 대해서 예약 대기 신청을 할 경우 예약이 된다.
- 자신의 예약 대기를 취소할 수 있다.
  - 자신의 예약 대기가 아닌 경우 취소할 수 없다.
- 나의 예약 목록을 조회할 수 있다.
  - 예약과 예약 대기를 나눠서 조회한다.
  - 예약은 reservation을 조회하고 예약 대기는 reservation-waiting을 조회한다.
  - 예약 대기의 경우 대기 순번도 함께 조회할 수 있다.

### 2단계 기능 구현 목록
- [x] 예약 대기 기능 구현
  - [x] 예약 대기 테이블 추가
    - [x] 예약 대기 상태 컬럼을 가짐
- [x] 예약 취소 기능 구현
- [x] 내 예약 목록 조회 기능 구현
  - [x] 예약과 예약 대기를 나눠서 조회
  - [x] 예약은 reservation을 조회하고 예약 대기는 reservation-waiting을 조회
  - [x] 예약 대기의 경우 대기 순번도 함께 조회
  - [x] 예약 순번 조회시 해당 scheduleId 중 몇 번째 대기자인지 반환


### 리팩토링 목록
- [x] Reservation List 를 ReservationResponse List로 변환하는 메서드 추가
- [x] ReservationService의 findMyReservations에서 예외 처리 개선
  - [x] ReservationWaiting에도 같은 작업 수행
- [x] ReservationResponse DTO에서 Schedule 대신 ScheduleResponse를 의존하도록 개선
- [x] ReservationWaitingService의 예약 대기 생성 기능에 트랜잭션 처리


### 3단계 기능 구현 목록
- [ ] 예약 상태 구현 
  - `예약 미승인`
  - `예약 승인`
  - `예약 취소`
  - `예약 취소 대기`
  - `예약 거절`
- [ ] 예약 승인 기능 구현 (사용자)
  - [ ] 예약 상태는 `예약 승인`으로
- [ ] 예약 취소 기능 구현 (사용자/관리자)
  - [ ] `예약 미승인` 상태인 경우 - `예약 취소`
  - [ ] `예약 승인` 상태인 경우 - `예약 취소 대기`
- [ ] 예약 거절 기능 구현 (관리자)
  - [ ] `예약 미승인` 상태인 경우 -> `예약 거절`
  - [ ] `예약 승인` 상태인 경우 -> `예약 거절`
- [ ] 예약 취소 승인 기능 (관리자)
  - [ ] `예약 취소 대기` -> `예약 취소`
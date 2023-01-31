# spring-roomesacpe-waiting

## 1단계 요구사항
- [x] auth 패키지를 nextstep 패키지로부터 분리한다
  - [x] 기존에 Component Scan으로 등록하던 객체를 Java Configuration을 이용하여 등록한다.
  - [x] LoginController와 LoginService 객체도 Java Configuration을 이용하여 등록해야 한다.
- [x] auth 패키지에서 nextstep로 의존하는 부분을 제거한다.
- [x] auth 패키지 내에서 스프링 빈으로 사용되던 객체를 Component Scan이 아닌 Java Configuration으로 빈 등록한다.

## 2단계 요구사항
### 예약 대기 신청
- [x] 이미 예약이 된 스케줄 대상으로 예약 대기를 신청할 수 있다.
- [x] 예약이 없는 스케줄에 대해서 예약 대기 신청을 할 경우 예약이 된다.

### 예약 대기 취소

- [x] 자신의 예약 대기를 취소할 수 있다.
- [x] 자신의 예약 대기가 아닌 경우 취소할 수 없다.

### 나의 예약 조회

- [x] 나의 예약 목록을 조회할 수 있다.
  - [x] 예약과 예약 대기를 나눠서 조회한다.
  - [x] 예약은 reservation을 조회하고 예약 대기는 reservation-waiting을 조회한다.
  - [x] 예약 대기의 경우 대기 순번도 함께 조회할 수 있다.

### 1,2 단계 기능 추가

- [ ] 테마의 가격은 양수로만 등록이 가능하다.
- [ ] 테마의 삭제 시 연관된 모든 스케줄이 삭제된다.
  - [ ] 스케줄을 삭제할 수 없는 경우 테마를 삭제할 수 없다.
  - [ ] 특정일 이후로의 예약을 차단할 수 있다.


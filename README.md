## spring-roomesacpe-waiting
#### 1단계 - 인증 로직 리팩터링
##### 프로그래밍 요구사항
- [x] auth 패키지를 nextstep 패키지로부터 분리한다
  - [x] auth 패키지에서 nextstep로 의존하는 부분을 제거한다.
  - [x] auth 패키지 내에서 스프링 빈으로 사용되던 객체를 Component Scan이 아닌 Java Configuration으로 빈 등록한다.

#### 2단계 - 예약 대기 기능(reservations-waitings)
##### 기능 요구사항
###### 예약 대기 신청
- [x] 이미 예약이 된 스케줄 대상으로 예약 대기를 신청할 수 있다.
  - [x] 예약이 없는 스케줄에 대해서 예약 대기 신청을 할 경우 예약이 된다.
- reservation 테이블에서 동일한 schedule_id를 가지는 예약이 존재하는지 찾는다.
- 있다면 예약 status 를 WAITING으로, 없다면 CONFIRMED로 테이블에 삽입한다.

###### 예약 대기 취소
- [x] 자신의 예약 대기를 취소할 수 있다.
  - [x] 자신의 예약 대기가 아닌 경우 취소할 수 없다.
- [x] 예약을 취소할 경우 예약 대기 중 대기 번호가 가장 작은 대기가 예약이 된다.
- reservation에서 예약을 삭제한다.
- 삭제된 예약과 동일한 schedule_id를 가지는 예약 중 가장 번호가 작은 예약을 찾아 status를 CONFIRMED로 변경한다.


###### 나의 예약 조회
- [x] 나의 예약 목록을 조회할 수 있다.
  - [x] 예약과 예약 대기를 나눠서 조회한다.
  - [x] 예약 조회: reservations/mine
  - [x] 예약 대기 조회: reservations-waitings/mine을 조회한다.
  - [x] 예약 대기의 경우 대기 순번도 함께 조회할 수 있다.
- reservation에서 사용자의 모든 예약을 찾은 다음, status를 이용하여 필터링한다.
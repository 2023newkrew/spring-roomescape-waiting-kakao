# spring-roomescape-waiting

--- 
- 요구사항
    - [x] auth 패키지를 nextstep 패키지로부터 분리한다.
      - [x] auth 패키지에서 nextstep로 의존하는 부분을 제거한다.
      - [x] auth 패키지 내에서 스프링 빈으로 사용되던 객체를 Component 스캔이 아닌 Java Configuration으로 빈 등록한다.
    - [x] 이미 예약이 된 스케줄 대상으로 예약 대기를 신청할 수 있다.
      - [x] 예약이 없는 스케줄에 대해서 예약 대기 신청을 할 경우 예약이 된다.
    - [x] 자신의 예약 대기를 취소할 수 있다.
      - [x] 자신의 예약 대기가 아닌 경우 취소할 수 없다.
    - [x] 나의 예약 목록을 조회할 수 있다.
      - [x] 예약과 예약 대기를 나눠서 조회한다.
      - [x] 예약은 reservation을 조회하고 예약 대기는 reservation-wating을 조회한다.
      - [x] 예약 대기의 경우 대기 순번도 함께 조회할 수 있다.
  
- DB 설계
  - Table. Schedule, member_id, priority
    - Table. ADD Schedule, member_id, (GET MAX priority in Scedule ++)
    - Table. DELETE Schedule, member_id
    - 예약 취소 시 예약 대기 순번이 가장 빠른 예약 대기를 예약으로 전환한다.
    - 예약 대기 순번은 현재까지 발급된 대기 순번 다음 번호로 발급되며 고정된다(ex. 은행 대기번호).
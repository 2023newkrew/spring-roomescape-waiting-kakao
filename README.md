# spring-roomesacpe-waiting

## 프로그래밍 요구사항
- auth 패키지를 nextstep 패키지로부터 분리한다
  - [X] auth 패키지에서 nextstep로 의존하는 부분을 제거한다.
  - [X] auth 패키지 내에서 스프링 빈으로 사용되던 객체를 Component Scan이 아닌 Java Configuration으로 빈 등록한다.
- 예약 대기 신청
  - [X] 이미 예약이 된 스케줄 대상으로 예약 대기를 신청할 수 있다.
  - [X] 예약이 없는 스케줄에 대해서 예약 대기 신청을 할 경우 예약이 된다.

## 패키지 분리 방법
- auth 패키지에 멤버 정보를 조회할 때 사용할 인터페이스 구현
- nextStep 패키지에 인터페이스 구현체를 생성 (구현체에는 멤버 조회를 위해 MemberService가 사용)
- auth 패키지의 컴포넌트 등록을 위해 nextstep 패키지의 AuthConfig를 통해 등록

## 예약 대기 신청 구현 방법
- ReservationWaiting 관련 Controller, Service, Dao, Entity, Dto 추가 (Reservation과 거의 유사)
- 예약 대기를 신청할 때 Controller에서 ReservationService를 통해 우선 예약 생성 시도, 실패하면 ReservationWaitingService를 통해 예약 대기 생성
- 예약을 삭제할 때 event 발생을 통해 삭제된 예약의 스케줄로 예약 대기가 존재한다면 가장 먼저 등록된(id가 제일 작은) 예약 대기를 예약으로 전환
- 자신의 예약 대기 순번을 조회할 때는 dao 계층에서 sql을 통해 순번 조회 (주어진 스케줄 Id로 대기 중인 예약 대기 전체를 조회한 다음에 본인 id의 순서를 구하는 방법)
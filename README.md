# spring-roomescape-waiting

## 1단계

### 요구 사항
- auth 패키지를 nextstep 패키지로부터 분리한다 
  - auth 패키지에서 nextstep 로 의존하는 부분을 제거한다. (패키지 분리를 통해 패키지 의존성 분리) 
  - auth 패키지 내에서 스프링 빈으로 사용되던 객체를 Component Scan 이 아닌 Java Configuration 으로 빈 등록한다. (Component Scan 대신 빈 설정)

### 구현 목록
- [x] Auth 패키지를 nextstep 외부로 이동
- [x] member 의 의존성 제거 - 중간 객체 사용
- [x] memberDao 의 의존성 제거
- [x] Configuration, Import 사용해서 auth 패키지 내 컴포넌트들을 bean 으로 등록

---

## 2단계

### 요구 사항
- 예약 대기 신청
  - 이미 예약이 된 스케줄 대상으로 예약 대기를 신청할 수 있다.
  - 예약이 없는 스케줄에 대해서 예약 대기 신청을 할 경우 예약이 된다. 

- 예약 대기 취소
  - 자신의 예약 대기를 취소할 수 있다.
  - 자신의 예약 대기가 아닌 경우 취소할 수 없다.
  
- 나의 예약 조회
  - 나의 예약 목록을 조회할 수 있다.
  - 예약과 예약 대기를 나눠서 조회한다.
  - 예약은 reservation을 조회하고 예약 대기는 reservation-waiting을 조회한다.
  - 예약 대기의 경우 대기 순번도 함께 조회할 수 있다.

### 구현 목록

- [x] reservation-waiting table scheme 작성
- [x] reservation-waiting repository 구현
- [x] reservation-waiting service 구현
- [x] reservation-waiting controller 구현
  - [x] reservation-waiting post 구현
    - [x] 예약이 없는 스케쥴에 대해서 예약이 된다.
  - [x] reservation-waiting get 구현
    - [x] reservation-waiting 순번 조회 기능
  - [x] reservation-waiting delete 구현
    - [x] 자신의 reservation-waiting 이 아니면 취소가 불가능 하다.
- [x] 통합테스트 작성
- [x] 변하지 않는 값들 final 적용
- [x] Builder 적용
- [x] Validator 구현
- [x] dao sql 분리
- [x] dao interface 분리
- [x] schema comment 추가

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

* Pair Programming
- [x] reservation-waiting table scheme 작성
  - [x] ReservationWaiting 단위테스트 작성
- [x] reservation-waiting repository 구현
  - [x] jdbc test 작성
- [x] reservation-waiting service 구현
- [x] reservation-waiting controller 구현
  - [x] reservation-waiting post 구현
    - [x] 예약이 없는 스케쥴에 대해서 예약이 된다.
  - [x] reservation-waiting get 구현
    - [x] reservation-waiting 순번 조회 기능
  - [x] reservation-waiting delete 구현
    - [x] 자신의 reservation-waiting 이 아니면 취소가 불가능 하다.
- [x] 통합테스트 작성

* 개인 refactoring
- [x] 테스트 코드 정리
  - [x] 유지보수 관련 변수 정리
  - [x] 테스트 설명 추가
  - [x] 중복코드 분리
- [x] 적용되지 않은 예외 상황들 생각해보기
  - [x] 다른 사람의 예약 대기를 취소하려 하는 경우 예외처리가 잘 되어있나?
- [x] 예약 대기를 추가하면서 달라져야 하는 부분 생각해보기
  - [x] 기존의 예약을 취소하는 경우에 해당 스케줄에 예약 대기가 있으면 예약처리 
    - [x] 예약 취소시 예약 대기 테이블에 해당 스케줄에 해당하는 대기가 있는지 확인
      - [x] ReservationWaitingDao 에 findByScheduleId 추가
    - [x] 대기가 없으면 별도 처리 필요 없음
    - [x] 대기가 있다면 해당 멤버로 예약 처리
    - [x] 대기 목록에서 예약처리된 내역은 삭제
      - 실무에서는 삭제하기 보다는 처리되었음을 표시할 것 같음. 반영은 x.
- [x] member id 에 대해서 물리적인 외래키 적용해보기
  - 제약 조건
    - 과거 기록까지 테이블에 있다고 가정하면 member 가 지워지더라도 기록은 남기고 싶을 수 있음.
    - 하지만 미래 예약에 관한 건만 있다고 하면 member 가 탈퇴하면 예약과 대기는 취소되어야 함.
    - 따라서 member 의 삭제 시 예약과 대기가 취소되도록 on delete cascade 적용함.
    - 테스트는 기존 member 에 삭제가 제공되지 않고 있고, 만들려면 복잡해서 일단 생략..


## 리팩토링

### 리팩토링 목록
- SQL 문
  - [x] 가독성을 위해 대문자로 변환하기
- ReservationWaiting create
  - [x] 로직 상의 분기와 오류로 인한 예외 구분하기 - 예약 존재 여부에 따른 분기처리
- ReservationWaiting delete
  - [x] 애초에 존재하지 않거나, 본인의 것이 아니거나 하는 경우에 대해 따로 분기처리 하기 
  - [x] NullPointerException -> 의미에 맞게 바꾸기
- 예외처리
  - [x] Dao 에서 queryForObject 관련 try catch 부분 어떤 예외인지에 따라 별도 처리하기
    - record 가 없어서 발생하는 에러라면 null return
    - 문법 오류라면 예외 발생 & 로그 동반
    - 비슷하게 사용된 부분에 공통으로 사용할 수 있도록 고민해보기 - Spring AOP 적용
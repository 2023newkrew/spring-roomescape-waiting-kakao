omm# spring-roomescape-waiting

# Step 1
## 프로그래밍 요구사항
* [x] auth package를 nextstep 패키지로부터 분리한다
    * [x] auth 패키지에서 nextstep으로 의존하는 부분을 제거한다.
      * UserDetails, UserDetailService
    * [x] auth 패키지 내에서 스프링 빈으로 사용되던 객체를 Component Scan이 아닌 Java Configuration으로 빈 등록한다.

현재 auth 패키지에서 nextstep으로 의존하고 있는 곳 : LoginService(MemberDao 및 Member 의존, ID, PW 확인을 위하여 의존)

해결방안 1 : auth package에 UserAuthDetails와 같은 클래스를 만들어서 MemberDao에서 의존하게 만드는 것이 어떨까?



# Step 2
## 기능 요구사항
### 예약 대기 신청
* [x] 이미 예약이 된 스케줄 대상으로 예약 대기를 신청할 수 있다.
  * [x] 예약이 없는 스케줄에 대해서 예약 대기 신청을 할 경우 예약이 된다.

### 예약 대기 취소
* [x] 자신의 예약 대기를 취소할 수 있다.
    * [x] 자신의 예약 대기가 아닌 경우 취소할 수 없다.


### 나의 예약 조회
* [x] 나의 예약 목록을 조회할 수 있다.
    * [x] 예약과 예약 대기를 나눠서 조회한다.
    * [x] 예약은 reservation을 조회하고 예약 대기는 reservation-waiting을 조회한다.
    * [x] 예약 대기의 경우 대기 순번도 함께 조회할 수 있다.

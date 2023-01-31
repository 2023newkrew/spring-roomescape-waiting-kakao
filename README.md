# spring-roomesacpe-waiting

## Step1 인증 리팩토링

### 프로그래밍 요구사항

- auth 패키지를 nextstep 패키지로부터 분리
- auth 패키지에서 nextstep로 의존하는 부분을 제거(순환참조 해결)
    - UserDetails와 UserDetailsFactory를 중간객체로 사용
    - nextstep 패키지에서 UserDetailsFactory를 구현
    - LoginMemberArgumentResolver의 반환타입을 Member에서 UserDetails로 변경
- auth 패키지 내에서 스프링 빈으로 사용되던 객체를 Component Scan이 아닌 Java Configuration으로 빈 등록
    - UserDetailsFactory, JwtTokenProvider, LoginService, LoginController를 @Configuration을 통해 빈으로 등록

---
## Step2 예약 대기 기능
### 프로그래밍 요구사항
- 예약 대기 테이블 생성

- 예약 대기 신청
  - 존재하지 않는 예약에 예약 대기를 신청할 수 없다.(BAD_REQUEST)
  - 이미 예약이 된 스케줄 대상으로 예약 대기를 신청할 수 있다. 
  - 예약이 없는 스케줄에 대해서 예약 대기 신청을 할 경우 예약이 된다.
  - 자신이 예약한 스케줄에 대해서 예약 대기를 신청할 수 없다.(BAD_REQUEST)
  - 동일한 예약에 대하여 여러 개의 예약 대기를 신청할 수 없다.

- 예약 대기 취소 
  - 존재하지 않는 예약 대기를 취소할 수 없다(BAD_REQUEST) 
  - 자신의 예약 대기를 취소할 수 있다. 
  - 자신의 예약 대기가 아닌 경우 취소할 수 없다.(FORBIDDEN)

- 예약 취소
  - 예약 취소시 가장 대기번호가 작은 예약대기가 예약으로 전환된다.
  
- 나의 예약 조회 
  - 나의 예약 목록을 조회할 수 있다. 
  - 예약과 예약 대기를 나눠서 조회한다. 
  - 예약은 reservation을 조회하고 예약 대기는 reservation-waiting을 조회한다. 
  - 예약 대기의 경우 대기 순번(wait_num)도 함께 조회할 수 있다.
  - 대기 순번은 DB에 저장하지 않고 매 요청시 계산한다.

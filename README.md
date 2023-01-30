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

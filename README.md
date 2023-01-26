# spring-roomesacpe-waiting

## 프로그래밍 요구사항
- [] auth 패키지를 nextstep 패키지로부터 분리한다
  - [x] MemberDetails 구현
  - [x] MemberDetailsService 구현
    - [x] findByUsername 메서드 구현
  - [x] accessToken에 id, username, role 저장
  - [x] LoginService의 extractMember 메서드에서 findByUsername 재사용

- [] auth 패키지에서 nextstep로 의존하는 부분을 제거한다.
- [] auth 패키지 내에서 스프링 빈으로 사용되던 객체를 Component Scan이 아닌 Java Configuration으로 빈 등록한다.
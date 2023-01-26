# spring-roomesacpe-waiting

### 1단계 프로그래밍 요구사항
- auth 패키지를 nextstep 패키지로부터 분리한다
  - auth 패키지에서 nextstep로 의존하는 부분을 제거한다.
  - auth 패키지 내에서 스프링 빈으로 사용되던 객체를 Component Scan이 아닌 Java Configuration으로 빈 등록한다.

### 1단계 기능 구현 목록
- [ ] nextstep 패키지에서 auth 패키지 분리
  - [ ] auth 패키지에서 nextstep 패키지로의 의존성 제거
  - [ ] auth 패키지 내 스프링 빈을 Java Configuration으로 빈 등록

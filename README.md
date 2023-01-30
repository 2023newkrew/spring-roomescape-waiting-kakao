# spring-roomesacpe-waiting

### 기능 요구사항
- [ ] auth 패키지를 nextstep 패키지로부터 분리한다
  - [ ] auth 패키지에서 nextstep으로 의존하는 부분을 제거한다
    - [ ] auth 패키지의 모든 파일은 `import nextstep.*`이 없어야 한다
      - [ ] ApplicationEventListener를 통해 Auth -> Nextstep에 이벤트를 발행하여 알맞은 멤버인지에 대한 정보를 검증한다
      - [x] Auth에서는 member의 id 정보를 담은 UserDetails를 Nextstep 패키지에서 사용할 수 있도록 넘겨준다
  - [x] auth 패키지 내에서 스프링 빈으로 사용되던 객체를 Component Scan이 아니라 Java Configuration으로 빈 등록한다

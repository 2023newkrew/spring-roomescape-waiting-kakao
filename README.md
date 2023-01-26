# spring-roomesacpe-waiting

### 기능 요구사항
- [ ] auth 패키지를 nextstep 패키지로부터 분리한다
  - [ ] auth 패키지에서 nextstep으로 의존하는 부분을 제거한다
    - [ ] auth 패키지의 모든 파일은 `import nextstep.*`이 없어야 한다
  - [x] auth 패키지 내에서 스프링 빈으로 사용되던 객체를 Component Scan이 아니라 Java Configuration으로 빈 등록한다

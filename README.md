# spring-roomesacpe-waiting

# spring-roomesacpe-waiting

## 구현사항
- [ ] auth 패키지를 nextstep 패키지로부터 분리한다
  - [ ] auth 패키지에서 nextstep로 의존하는 부분을 제거한다.
  - [x] auth 패키지 내에서 스프링 빈으로 사용되던 객체를 Component Scan이 아닌 Java Configuration으로 빈 등록한다.
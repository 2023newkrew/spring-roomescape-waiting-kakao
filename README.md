# spring-roomesacpe-waiting

## 프로그래밍 요구사항
-[x] auth 패키지를 nextstep 패키지로부터 분리한다
  - [x] 기존에 Component Scan으로 등록하던 객체를 Java Configuration을 이용하여 등록한다.
  - [x] LoginController와 LoginService 객체도 Java Configuration을 이용하여 등록해야 한다.
-[ ] auth 패키지에서 nextstep로 의존하는 부분을 제거한다.
-[ ] auth 패키지 내에서 스프링 빈으로 사용되던 객체를 Component Scan이 아닌 Java Configuration으로 빈 등록한다.


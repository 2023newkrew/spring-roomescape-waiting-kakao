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
- [ ] Configuration 사용해서 auth 패키지 내 컴포넌트들을 bean 으로 등록


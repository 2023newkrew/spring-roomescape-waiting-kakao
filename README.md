# spring-roomesacpe-waiting

### 기능 요구사항
- [x] auth 패키지를 nextstep 패키지로부터 분리한다
  - [x] auth 패키지에서 nextstep으로 의존하는 부분을 제거한다
    - [x] auth 패키지의 모든 파일은 `import nextstep.*`이 없어야 한다
      - [x] ApplicationEventListener를 통해 Auth -> Nextstep에 이벤트를 발행하여 알맞은 멤버인지에 대한 정보를 검증한다
      - [x] Auth에서는 member의 id 정보를 담은 UserDetails를 Nextstep 패키지에서 사용할 수 있도록 넘겨준다
  - [x] auth 패키지 내에서 스프링 빈으로 사용되던 객체를 Component Scan이 아니라 Java Configuration으로 빈 등록한다
- [ ] 예약 대기 신청을 받을 수 있다
  - [ ] 이미 예약이 되어있는 스케줄의 경우, 예약 대기를 받을 수 있다
  - [ ] 예약이 되어있지 않은 스케줄의 경우, 예약 대기는 예약으로 처리한다
- [ ] 예약이 취소되었을 경우, 예약 대기 중 가장 빠른 예약 대기를 예약으로 변경한다
- [ ] 예약 대기 취소를 할 수 있다
  - [ ] 자신의 예약대기만을 취소할 수 있다
- [ ] 나의 예약 목록을 조회할 수 있다
  - [ ] 나의 예약을 조회할 수 있다.
  ```http request
  GET /reservations/mine HTTP/1.1
  authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
  ```
  ```http request
  HTTP/1.1 200 
  Content-Type: application/json
  
  [
      {
          "id": 1,
          "schedule": {
              "id": 1,
              "theme": {
                  "id": 1,
                  "name": "테마이름",
                  "desc": "테마설명",
                  "price": 22000
              },
              "date": "2022-08-11",
              "time": "13:00:00"
          }
      }
  ]
  ```
  - [ ] 나의 예약대기를 조회할 수 있다
    - [ ] 예약대기 조회 시, 대기 순번도 조회되어야 한다
  ```http request
  GET /reservation-waitings/mine HTTP/1.1
  authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
  ```
  ```http request
  HTTP/1.1 200 
  Content-Type: application/json
  
  [
      {
          "id": 1,
          "schedule": {
              "id": 3,
              "theme": {
                  "id": 2,
                  "name": "테마이름2",
                  "desc": "테마설명2",
                  "price": 20000
              },
              "date": "2022-08-20",
              "time": "13:00:00"
          },
          "waitNum": 12
      }
  ]
  ```
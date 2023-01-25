# Spring 방탈출

## 상황 설명

기존의 방탈출 예약 시스템에서 로그인 기능과 그에 따라 자기 자신의 예약만 취소 가능하고,
로그인해야만 예약 가능한 등의 인증 기능이 필요하다.
이를 위해 회원 관련 기능과 로그인 기능을 추가하고 기존의 몇몇 API를 수정해야 한다.

또한 유저정보에 관리자 권한을 추가로 기록하고 몇몇 API는 이를 검증해야 한다.
예를 들어 테마 생성, 삭제는 반드시 어드민만 가능해야 하고, 이 기능은 인터셉터를 이용해 구현되어야 한다.

## 요구사항

- 관리자 전용 API 테마 추가 구현

  > POST http://localhost:8080/theme -> POST http://localhost:8080/admin/theme
  >
  > 이 API는 반드시 `HTTP Authorization`헤더로 전달된 JWT 토큰의 claim에
  > is_admin값을 `true`로 가지고 있어야 한다.

- 관리자 전용 API 테마 삭제 구현

  > DELETE http://localhost:8080/theme/1 -> DELETE http://localhost:8080/admin/theme/1
  > 트
  > 이 API는 반드시 `HTTP Authorization`헤더로 전달된 JWT 토큰의 claim에
  > is_admin값을 `true`로 가지고 있어야 한다.

- 사용자 등록 기능 구현
    - 새로운 사용자가 등록될 수 있어야 한다.
  > 예시
  > ```http request
  > POST http://localhost:8080/member
  > Content-Type: application/json
  > 
  > {
  >   "name": "이름",
  >   "phone": "전화번호",
  >   "username": "유저명",
  >   "password": "비밀번호"
  > }
  > ```
  > 결과
  > ```
  > HTTP/1.1 201 Created
  > Content-Type: application/json
  > Location: /login/token/<JWT 토큰>
  > {
  >   "id": <생성된 멤버 ID>
  > }
  > ```
- 로그인 기능 구현
    - JWT 토큰을 통한 인증 기능을 구현해야 한다.
  > 예시
  > ```http request
  > POST http://localhost:8080/login/token
  > Content-Type: application/json
  > 
  > {
  >   "username": "유저명",
  >   "password": "비밀번호"
  > }
  > ```
  > 결과
  > ```
  > HTTP/1.1 201 Created
  > Content-Type: application/json
  > Location: /login/token/<JWT 토큰>
  > {
  >   "access_token": <JWT 토큰>
  > }
  > ```
- 예약 API를 인증 기능과 연동 가능하도록 구성하여야 한다.
  > 예시
  > ```http request
  > POST http://localhost:8080/reservations/
  > Authorization: Bearer <JWT Token>
  > Content-Type: application/json
  > 
  > {
  >   "name": "테마입니다",
  >   "desc": "설명입니다",
  >   "price": 111000
  > }
  > ```
- 예약 삭제 API를 인증 기능과 연동 가능하도록 구성하여야 한다.
  > 예시
  > ```http request
  > DELETE http://localhost:8080/reservations/1
  > Authorization: Bearer <JWT Token>
  > ```

--- 
> Spring Basic 이전 기록

## 상황 설명

- 기존에는 로컬 환경에서 콘솔 애플리케이션을 이용하여 예약 정보를 관리해왔다.
- 콘솔 애플리케이션을 웹 애플리케이션으로 전환하여 집에서도 웹을 통해 예약 관리를 할 수 있도록 해야한다.
- 기존 콘솔 어플리케이션은 아래와 같은 기능을 지원하고 있었다.

  ```
  ### 명령어를 입력하세요. ###
  예약하기: add {date},{time},{name} ex) add 2022-08-11,13:00,류성현
  예약조회: find {id} ex) find 1
  예약취소: delete {id} ex) delete 1
  종료: quit
  ```
  위와 같이 3개의 기능을 지원하며 각각 예약, 예약 조회, 예약 취소의 동작을 CLI 기반의 인터페이스로 수행 가능했다.
- 시간이 지나면서 시스템에 확장 요구사항이 생겼다. 확장은 예약을 테마별로 가능하게 하며, 테마를 관리(추가,삭제) 가능하게 구성해야 한다.

## 요구사항

- [X] 웹 요청 / 응답 처리로 입출력 추가
    - [X] 테마
        - [ ] 테마 생성
        - [ ] 테마 목록
        - [ ] 특정 테마 찾기
        - [ ] 테마 삭제
    - [X] 예약
        - [X] 예약 하기
          ```
          POST /reservations HTTP/1.1
          Content-Type: application/json; charset=UTF-8
          Host: localhost:8080
          {
            "date" : "2022-08-11",
            "time" : "12:34:56",
            "name" : "사람 이름",
            "theme_name" : "테마 이름",
            "theme_desc" : "테마 설명",
            "theme_price" : "테마 가격",
          }
          ```
          ```
          HTTP/1.1 201 Created
          Location: /reservations/1
          ```
        - [X] 예약 조회
          ```
          GET /reservations/1 HTTP/1.1
          ```
          ```
          HTTP/1.1 200 
          Content-Type: application/json
          {
            "id": 1,
            "date": "2022-08-11",
            "time": "13:00",
            "name": "name",
            "themeName": "워너고홈",영
            "themeDesc": "병맛 어드벤처 회사 코믹물",
            "themePrice": 29000
          }
          ```
        - [X] 예약 취소
          ```
          DELETE /reservations/1 HTTP/1.1
          ```
          ```
          HTTP/1.1 204 
          ```
- [X] 예외 처리
    - [X] 예약 생성) content-type이 application/json이 아닌 경우 값을 받지 않는다.
    - [X] 예약 생성) 예약 생성 시 날짜와 시간이 똑같은 예약이 이미 있는 경우 예약을 생성할 수 없다.
    - [X] 예약 생성) 값이 포함되지 않았을 경우 예약 생설 불가
    - [X] 예약 생성) 값의 포맷이 맞지 않을 경우 생성 불가
    - [X] 예약 조회) ID가 없는 경우 조회 불가
    - [X] 예약 조회) ID가 잘못된 경우 (float, string)
    - [X] 예약 삭제) ID가 없는 경우 삭제 불가
    - [X] 예약 삭제) ID가 잘못된 경우 (float, string)
- [X] 웹 애플리케이션에 데이터베이스를 적용한다.
    -  [X] 스프링이 제공하는 기능을 활용하여 데이터베이스에 접근한다.
        - [X] 예약 생성
        - [X] 예약 조회
        - [X] 예약 삭제

## 프로그래밍 요구사항

- 기존 콘솔 애플리케이션은 그대로 잘 동작해야한다.
- 콘솔 애플리케이션과 웹 애플리케이션의 중복 코드는 허용한다. (다음 단계에서 리팩터링 예정)
- 콘솔 애플리케이션과 웹 애플리케이션에서 각각 데이터베이스에 접근하는 로직을 구현한다.
- 콘솔 애플리케이션에서 데이터베이스에 접근 시 JdbcTemplate를 사용하지 않는다. 직접 Connection을 생성하여 데이터베이스에 접근한다.
- 웹 애플리케이션에서 데이터베이스 접근 시 JdbcTemplate를 사용한다.
- 콘솔 애플리케이션과 웹 애플리케이션의 중복 코드 제거해본다.

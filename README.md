# spring-roomesacpe-waiting

---

# Step 1

## 프로그래밍 요구사항
- [x] auth 패키지를 nextstep 패키지로부터 분리한다.
    - [x] auth 패키지에서 nextstep로 의존하는 부분을 제거한다.
    - [x] auth 패키지 내에서 스프링 빈으로 사용되던 객체를 Component Scan이 아닌 Java Configuration으로 빈 등록한다.

## 요구사항 설명

### 패키지 분리
- auth 패키지를 nextstep 패키지로부터 분리한다.
    - to be 패키지 구조
    - ![img.png](img.png)

### Java Configuration 활용
- 기존에 Component Scan으로 등록하던 객체를 Java Configuration을 이용하여 등록한다.
- LoginController와 LoginService 객체도 Java Configuration을 이용하여 등록해야 한다.

# Step 2

## 기능 요구사항
- 예약 대기 신청 
  - [x] 이미 예약이 된 스케줄 대상으로 예약 대기를 신청할 수 있다.
    - [x] 예약이 없는 스케줄에 대해서 예약 대기 신청을 할 경우 예약이 된다.
- 예약 대기 취소 
  - [x] 자신의 예약 대기를 취소할 수 있다. 
    - [x] 자신의 예약 대기가 아닌 경우 취소할 수 없다.
- 나의 예약 조회 
  - [x] 나의 예약 목록을 조회할 수 있다. 
    - [x] 예약과 예약 대기를 나눠서 조회한다. 
    - [x] 예약은 reservation을 조회하고 예약 대기는 reservation-waiting을 조회한다. 
    - [x] 예약 대기의 경우 대기 순번도 함께 조회할 수 있다.

## API 설계

### 예약 대기
```
POST /reservation-waitings HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
content-type: application/json; charset=UTF-8
host: localhost:8080

{
    "scheduleId": 1
}
```

```
HTTP/1.1 201 Created
Location: /reservation-waitings/1
```

### 예약 목록 조회
```
GET /reservations/mine HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
```

```
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

### 예약 대기 목록 조회
```
GET /reservation-waitings/mine HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
```

```
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

### 예약 대기 취소
```
DELETE /reservation-waitings/1 HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk5MDcwLCJleHAiOjE2NjMzMDI2NzAsInJvbGUiOiJBRE1JTiJ9.zgz7h7lrKLNw4wP9I0W8apQnMUn3WHnmqQ1N2jNqwlQ
```

```
HTTP/1.1 204 
```

# Step 3

## 기능 요구사항
- 예약 상태 
  - [ ] 예약 상태 구현 
    - 기존에는 예약에 상태가 없었지만 관리자의 승인이 필요해져서 상태 관리가 필요하다. 
    - 예약 상태는 **예약 미승인**, **예약 승인**, **예약 취소**, **예약 취소 대기**, **예약 거절** 상태가 있다.
- 예약 상태 
  - [ ] 예약 승인 기능 구현 
    - 사용자가 예약을 신청 후 관리자는 예약금 입금 확인을 하고 예약 승인을 한다
    - 예약 승인 시 예약의 상태는 **예약 승인**으로 변경된다. 
    - 예약금이 매출로 기록된다.
- 예약 취소 
  - [ ] 사용자의 예약 취소 기능 구현 
    - 사용자가 예약을 취소하는 경우 
    - 예약이 **예약 미승인** 상태인 경우 예약이 자동으로 취소되어 **예약 취소**가 된다. 
    - 예약 미승인 상태에서 취소하는 경우 예약금을 입금하지 않았다고 가정하여 매출 기록의 변경은 없다. 
    - 예약이 **예약 승인** 상태인 경우 관리자의 승인이 필요하여 **예약 취소 대기**가 된다.
- 예약 거절 
  - [ ] 관리자의 예약 거절 기능 구현 
    - 관리자가 예약을 거절하는 경우 
    - 예약이 **예약 미승인** 상태인 경우 예약이 취소되어 **예약 거절**이 된다. 
    - 예약이 **예약 승인** 상태인 경우 예약이 취소되어 **예약 거절**이 된다. 이 때 예약금 환불이 발생하여 매출 기록의 변경이 있다.
- 예약 취소 승인 
  - [ ] 관리자의 예약 취소 승인 기능 구현 
    - 사용자가 예약을 취소 한 후 관리자가 예약 취소를 승인하는 경우 
    - 예약이 **예약 취소 대기**에서 **예약 취소**로 변경된다. 
    - 매출에 예약금 환불 이력이 추가된다.

## API 설계

### 예약 승인 (관리자)
```
PATCH /reservations/1/approve HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
content-type: application/json; charset=UTF-8
host: localhost:8080
```

```
HTTP/1.1 200
```

### 예약 취소 (사용자 + 관리자)
```
PATCH /reservations/1/cancel HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
```

```
HTTP/1.1 200 
```

### 예약 취소 승인 (관리자)
```
GET /reservations/1/cancel-approve HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
```

```
HTTP/1.1 200 
```
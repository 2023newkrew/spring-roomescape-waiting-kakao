<<<<<<<< HEAD:src/main/java/nextstep/domain/dto/request/MemberRequest.java
package nextstep.domain.dto.request;
========
package nextstep.domain.dto;
>>>>>>>> 59193cb (refactor: 패키지 구조 변경):src/main/java/nextstep/domain/dto/MemberRequest.java

import nextstep.domain.persist.Member;

public class MemberRequest {
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    public MemberRequest(String username, String password, String name, String phone, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public Member toEntity() {
        return new Member(username, password, name, phone, role);
    }
}

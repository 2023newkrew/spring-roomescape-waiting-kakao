package app.nextstep.dto;

import app.nextstep.domain.Member;

public class MemberRequest {
    private String username;
    private String password;
    private String role;
    private String name;
    private String phone;

    public MemberRequest(String username, String password, String name, String phone, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.phone = phone;
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

    public Member toMember() {
        return new Member(null, username, password, role, name, phone);
    }
}

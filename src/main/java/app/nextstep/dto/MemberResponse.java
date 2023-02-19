package app.nextstep.dto;

import app.nextstep.domain.Member;

public class MemberResponse {
    private Long id;
    private String username;
    private String password;
    private String role;
    private String name;
    private String phone;

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.password = member.getPassword();
        this.role = member.getRole();
        this.name = member.getName();
        this.phone = member.getPhone();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}

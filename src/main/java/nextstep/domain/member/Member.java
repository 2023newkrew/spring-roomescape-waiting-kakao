package nextstep.domain.member;

import java.util.Objects;

public class Member {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    public Member() {
    }

    public Member(Long id, String username, String password, String name, String phone, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public Member(String username, String password, String name, String phone, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public boolean hasSameId(Long memberId) {
        return Objects.equals(id, memberId);
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

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

}

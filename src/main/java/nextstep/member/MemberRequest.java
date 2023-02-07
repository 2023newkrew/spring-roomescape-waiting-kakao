package nextstep.member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MemberRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String phone;
    @NotNull
    private Role role;

    public MemberRequest(String username, String password, String name, String phone, Role role) {
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

    public Role getRole() {
        return role;
    }

    public Member toEntity() {
        return new Member(username, password, name, phone, role);
    }
}

package nextstep.domain;

import auth.domain.Role;
import auth.domain.UserDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Member {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private Role role;

    public Member(UserDetails userDetails) {
        this.id = userDetails.getId();
        this.username = userDetails.getUsername();
        this.password = userDetails.getPassword();
        this.name = userDetails.getName();
        this.phone = userDetails.getPhone();
        this.role = userDetails.getRole();
    }

    public Member(String username, String password, String name, String phone, Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }
}

package nextstep.member;

import auth.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private Role role;

    @Builder
    public Member(Long id, String username, String password, String name, String phone, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }


    public boolean doesPasswordMatch(String password) {
        return this.password.equals(password);
    }
}

package nextstep.member.domain;

import auth.domain.UserDetails;
import auth.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
public class MemberEntity implements UserDetails {

    @Getter
    @Setter
    private Long id;

    @Getter
    private final String username;

    @Getter
    private final String password;

    @Getter
    private final String name;

    @Getter
    private final String phone;

    @Getter
    private final UserRole role;

    @Override
    public boolean isWrongPassword(String password) {
        return !Objects.equals(this.password, password);
    }

    @Override
    public boolean isNotAdmin() {
        return role != UserRole.ADMIN;
    }
}

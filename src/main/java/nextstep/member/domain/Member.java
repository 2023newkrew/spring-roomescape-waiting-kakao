package nextstep.member.domain;

import auth.domain.UserRole;
import lombok.Value;

@Value
public class Member {
    Long id;
    String username;
    String password;
    String name;
    String phone;
    UserRole role;

    public MemberEntity toEntityWithRole(UserRole role) {
        return new MemberEntity(id, username, password, name, phone, role);
    }
}

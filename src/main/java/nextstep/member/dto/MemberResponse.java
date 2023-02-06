package nextstep.member.dto;

import auth.domain.UserRole;
import lombok.Value;

@Value
public class MemberResponse {
    Long id;
    String username;
    String password;
    String name;
    String phone;
    UserRole role;
}

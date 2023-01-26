package nextstep.member.dto;

import auth.domain.UserRole;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class MemberResponse {

    private final Long id;

    private final String username;

    private final String password;

    private final String name;

    private final String phone;

    private final UserRole role;
}

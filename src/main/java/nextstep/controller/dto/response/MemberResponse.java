package nextstep.controller.dto.response;

import auth.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private String username;
    private String name;
    private String phone;
    private Role role;
}

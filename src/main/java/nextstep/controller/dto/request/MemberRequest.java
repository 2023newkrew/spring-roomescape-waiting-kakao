package nextstep.controller.dto.request;

import auth.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.domain.Member;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class MemberRequest {
    private String username;
    private String password;
    private String name;
    private String phone;

    public Member toEntity() {
        return new Member(username, password, name, phone, Role.USER);
    }
}

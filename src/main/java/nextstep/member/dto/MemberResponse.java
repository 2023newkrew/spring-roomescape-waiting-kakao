package nextstep.member.dto;

import auth.domain.UserRole;
import auth.dto.UserDetailsResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


@Setter(AccessLevel.NONE)
public class MemberResponse extends UserDetailsResponse {

    @Getter
    private final String name;

    @Getter
    private final String phone;

    public MemberResponse(Long id, String username, String password, UserRole role, String name, String phone) {
        super(id, username, password, role);
        this.name = name;
        this.phone = phone;
    }
}

package nextstep.controller.dto;

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
public class MemberResponse {
    private String username;
    private String name;
    private String phone;
    private Role role;

    public MemberResponse(UserDetails userDetails) {
        this.username = userDetails.getUsername();
        this.name = userDetails.getName();
        this.phone = userDetails.getPhone();
        this.role = userDetails.getRole();
    }
}

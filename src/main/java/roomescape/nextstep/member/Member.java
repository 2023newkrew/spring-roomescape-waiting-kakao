package roomescape.nextstep.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import roomescape.auth.UserDetails;

@Builder
@AllArgsConstructor
@Getter
public class Member implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    public boolean checkWrongPassword(String password) {
        return !this.password.equals(password);
    }
}

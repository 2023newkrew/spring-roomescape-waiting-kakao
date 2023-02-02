package auth.domain.persist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.domain.persist.Member;

@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDetails {
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

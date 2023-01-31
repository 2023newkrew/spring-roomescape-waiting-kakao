package nextstep.member;

import auth.login.UserDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    public Member(String username, String password, String name, String phone, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public static UserDetails convertToUserDetails(final Member member) {
        return UserDetails.builder()
            .id(member.getId())
            .role(member.getRole())
            .build();
    }

    public boolean checkWrongPassword(final String password) {
        return !this.password.equals(password);
    }
}

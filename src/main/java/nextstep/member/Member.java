package nextstep.member;

import auth.UserDetails;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

    public static Member from(UserDetails userDetails) {
        return new Member(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getName(),
                userDetails.getPhone(),
                userDetails.getRole()
        );
    }

    public boolean checkWrongPassword(String password) {
        return !this.password.equals(password);
    }
}

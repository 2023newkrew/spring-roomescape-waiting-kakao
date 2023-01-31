package nextstep.domain.persist;

import auth.domain.persist.UserDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    public Member(UserDetails userDetails) {
        this.id = userDetails.getId();
        this.username = userDetails.getUsername();
        this.password = userDetails.getPassword();
        this.name = userDetails.getName();
        this.phone = userDetails.getPhone();
        this.role = userDetails.getRole();
    }

    public Member(String username, String password, String name, String phone, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }
}

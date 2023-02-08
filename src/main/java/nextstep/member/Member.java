package nextstep.member;

import auth.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Member implements UserDetails {

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

    @Override
    public boolean checkWrongPassword(String password) {
        return !Objects.equals(this.password, password);
    }

    @JsonIgnore
    public boolean isAdmin() {
        return Objects.equals(this.getRole(), "ADMIN");
    }
}

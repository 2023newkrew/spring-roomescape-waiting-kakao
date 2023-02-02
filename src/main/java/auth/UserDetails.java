package auth;

import nextstep.member.Member;

public class UserDetails {
    private Long id;
    private String password;
    private String role;

    public UserDetails() {
    }

    public UserDetails(Long id, String password, String role) {
        this.id = id;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public boolean checkWrongPassword(String password) {
        return !this.password.equals(password);
    }

    public static UserDetails of(Member member) {
        return new UserDetails(member.getId(), member.getPassword(), member.getRole());
    }
}

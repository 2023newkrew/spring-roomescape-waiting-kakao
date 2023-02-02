package nextstep.member;

import auth.UserDetails;

public class LoginMember extends UserDetails {
    private LoginMember(Long id, String username, String password, String name, String phone, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public static LoginMember fromMember(Member member) {
        return new LoginMember(
                member.getId(),
                member.getUsername(),
                member.getPassword(),
                member.getName(),
                member.getPhone(),
                member.getRole()
        );
    }

    public Member toEntity() {
        return new Member(
                this.id,
                this.username,
                this.password,
                this.name,
                this.phone,
                this.role
        );
    }
}

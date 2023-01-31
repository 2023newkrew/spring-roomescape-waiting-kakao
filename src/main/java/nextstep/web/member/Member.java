package nextstep.web.member;

import auth.login.MemberDetail;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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

    public MemberDetail toMemberDetail() {
        return new MemberDetail(id, username, password, name, phone, role);
    }

    public static Member fromMemberDetail(MemberDetail memberDetail) {
        return new Member(
                memberDetail.getId(),
                memberDetail.getUsername(),
                memberDetail.getPassword(),
                memberDetail.getName(),
                memberDetail.getPhone(),
                memberDetail.getRole()
        );
    }
}

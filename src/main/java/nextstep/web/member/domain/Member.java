package nextstep.web.member.domain;

import auth.login.AbstractMember;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Member extends AbstractMember {
    private String name;
    private String phone;

    public Member(String username, String password, String name, String phone, String role) {
        super(username, password, role);
        this.name = name;
        this.phone = phone;
    }

    public Member(Long id, String username, String password, String name, String phone, String role) {
        super(id, username, password, role);
        this.name = name;
        this.phone = phone;
    }

    public Member() {
        super();
    }
}

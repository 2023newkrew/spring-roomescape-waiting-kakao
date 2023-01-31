package nextstep.member.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Member {
    private Long id;
    private String memberName;
    private String password;
    private String name;
    private String phone;

    public Member(Long id, String memberName, String password, String name, String phone) {
        this.id = id;
        this.memberName = memberName;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public Member(String memberName, String password, String name, String phone) {
        this.memberName = memberName;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public Member() {
    }
}

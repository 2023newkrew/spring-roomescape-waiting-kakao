package nextstep.member.model;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class MemberRequest {
    @NotBlank
    private String memberName;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String phone;

    public MemberRequest(String memberName, String password, String name, String phone) {
        this.memberName = memberName;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public Member toEntity() {
        return new Member(memberName, password, name, phone);
    }
}

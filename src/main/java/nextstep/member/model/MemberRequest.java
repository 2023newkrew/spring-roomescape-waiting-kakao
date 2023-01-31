package nextstep.member.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@NoArgsConstructor
public class MemberRequest {
    @NotBlank
    private String memberName;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String phone;

    public MemberRequest(Member member){
        this.memberName = member.getMemberName();
        this.password = member.getPassword();
        this.name = member.getName();
        this.phone = member.getPhone();
    }

    public Member toEntity() {
        return new Member(memberName, password, name, phone);
    }
}

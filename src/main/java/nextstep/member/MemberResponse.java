package nextstep.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberResponse {

    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;

    public static MemberResponse of(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getUsername(),
                member.getPassword(),
                member.getName(),
                member.getPhone()
        );
    }
}

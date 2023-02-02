package nextstep.member;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberResponse {
    private final Long id;
    private final String username;
    private final String name;
    private final String phone;
    private final Role role;

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getUsername(), member.getName(), member.getPhone(), member.getRole());
    }
}

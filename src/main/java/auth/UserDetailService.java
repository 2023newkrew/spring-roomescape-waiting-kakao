package auth;

import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.MemberDao;

@RequiredArgsConstructor
public class UserDetailService {

    private final MemberDao memberDao;

    public UserDetail getUserDetailByUsername(String username) {
        Member member = memberDao.findByUsername(username);
        if (member == null) {
            throw new AuthenticationException();
        }
        return new UserDetail(member);
    }

    public Member getMemberById(Long id) {
        return memberDao.findById(id);
    }
}

package auth;

import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.MemberDao;

@RequiredArgsConstructor
public class UserDetailService {

    private final MemberDao memberDao;

    public UserDetail getUserDetailByUsername(String username) {
        return new UserDetail(memberDao.findByUsername(username));
    }

    public Member getMemberById(Long id) {
        return memberDao.findById(id);
    }
}

package nextstep.member;

import auth.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class LoginMemberService implements UserDetailsService<Member, Long> {
    private final MemberDao memberDao;

    public LoginMemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public Member findByUsername(String username) {
        return memberDao.findByUsername(username);
    }

    @Override
    public Member findById(Long id) {
        return memberDao.findById(id);
    }
}

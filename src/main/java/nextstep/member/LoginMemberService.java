package nextstep.member;

import auth.UserDetails;
import auth.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class LoginMemberService implements UserDetailsService {
    private final MemberDao memberDao;

    public LoginMemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public UserDetails findByUsername(String username) {
        Member member = memberDao.findByUsername(username);
        return LoginMember.fromMember(member);
    }

    @Override
    public UserDetails findById(Long id) {
        Member member = memberDao.findById(id);
        return LoginMember.fromMember(member);
    }
}

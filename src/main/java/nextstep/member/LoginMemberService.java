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
        return memberDao.findByUsername(username);
    }

    @Override
    public UserDetails findById(Long id) {
        return memberDao.findById(id);
    }
}

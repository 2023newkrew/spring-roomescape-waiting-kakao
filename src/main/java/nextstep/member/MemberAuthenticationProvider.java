package nextstep.member;

import auth.AuthenticationProvider;
import auth.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberAuthenticationProvider implements AuthenticationProvider {
    private final MemberDao memberDao;

    @Override
    public UserDetails findById(Long id) {
        return createUserDetails(memberDao.findById(id));
    }

    @Override
    public UserDetails findByUsername(String username) {
        return createUserDetails(memberDao.findByUsername(username));
    }

    private UserDetails createUserDetails(Member member) {
        return new UserDetails(member.getId(), member.getPassword(), member.getRole());
    }
}

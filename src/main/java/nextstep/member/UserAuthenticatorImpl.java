package nextstep.member;

import auth.AuthenticationException;
import auth.UserAuthenticator;
import auth.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserAuthenticatorImpl implements UserAuthenticator {
    private final MemberDao memberDao;

    @Override
    public UserDetails authenticate(String username, String password) {
        Member member = memberDao.findByUsername(username).orElseThrow(AuthenticationException::new);
        if (member.checkWrongPassword(password)) {
            throw new AuthenticationException();
        }
        return new UserDetails(member.getId(), member.getRole());
    }
}

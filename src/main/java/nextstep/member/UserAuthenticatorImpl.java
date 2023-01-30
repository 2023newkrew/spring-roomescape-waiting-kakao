package nextstep.member;

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
        Member member = memberDao.findByUsername(username);
        if (member == null || member.checkWrongPassword(password)) {
            return null;
        }
        return new UserDetails(member.getId(), member.getRole());
    }
}

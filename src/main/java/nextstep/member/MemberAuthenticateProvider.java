package nextstep.member;

import auth.UserDetails;
import auth.AuthenticateProvider;
import lombok.RequiredArgsConstructor;
import nextstep.support.exception.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberAuthenticateProvider implements AuthenticateProvider {

    private final MemberDao memberDao;

    @Override
    public UserDetails createUserDetails(String username, String password) {
        Member member = memberDao.findByUsername(username);
        if (member == null || member.checkWrongPassword(password)) {
            throw new AuthenticationException();
        }
        return Member.convertToUserDetails(member);
    }

    @Override
    public UserDetails createUserDetails(Long principal, String role) {
        return UserDetails.builder()
                .id(principal)
                .role(role)
                .build();
    }
}

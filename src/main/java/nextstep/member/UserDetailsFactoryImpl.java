package nextstep.member;

import static nextstep.exception.ErrorMessage.NOT_AUTHORIZED;

import auth.login.UserDetails;
import auth.login.UserDetailsFactory;
import lombok.RequiredArgsConstructor;
import nextstep.exception.AuthenticationException;

@RequiredArgsConstructor
public class UserDetailsFactoryImpl implements UserDetailsFactory {

    private final MemberDao memberDao;

    @Override
    public UserDetails makeUserDetails(String username, String password) {
        Member member = memberDao.findByUsername(username);
        if (member == null || member.checkWrongPassword(password)) {
            throw new AuthenticationException(NOT_AUTHORIZED.getMessage());
        }
        return Member.convertToUserDetails(member);
    }

    @Override
    public UserDetails convertToUserDetails(Long id, String role) {
        return UserDetails.builder()
                .id(id)
                .role(role)
                .build();
    }
}

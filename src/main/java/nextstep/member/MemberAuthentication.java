package nextstep.member;

import auth.AuthenticationException;
import auth.AuthenticationProvider;
import auth.TokenRequest;
import auth.UserDetails;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MemberAuthentication implements AuthenticationProvider {

    private final MemberDao memberDao;

    @Override
    public UserDetails getUserDetails(TokenRequest tokenRequest){
        // 검증 로직
        Member member = memberDao.findByUsername(tokenRequest.getUsername());
        if (member == null || member.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        return new UserDetails(member.getId(), member.getRole());
    }
}

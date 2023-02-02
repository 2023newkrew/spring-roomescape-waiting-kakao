package nextstep.member;

import auth.utils.AuthenticationException;
import auth.utils.AuthenticationProvider;
import auth.dto.TokenRequest;
import auth.dto.UserDetails;
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

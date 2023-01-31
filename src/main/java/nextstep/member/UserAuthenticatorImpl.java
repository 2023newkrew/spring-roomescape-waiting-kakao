package nextstep.member;

import auth.AuthenticationException;
import auth.UserAuthenticator;
import auth.UserDetails;
import lombok.RequiredArgsConstructor;
import nextstep.support.DoesNotExistEntityException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserAuthenticatorImpl implements UserAuthenticator {
    private final MemberService memberService;

    @Override
    public UserDetails authenticate(String username, String password) {
        Member member;
        try {
            member = memberService.findByUsernameAndPassword(username, password);
        } catch (DoesNotExistEntityException e) {
            throw new AuthenticationException();
        }

        return new UserDetails(member.getId(), member.getRole().name());
    }

    @Override
    public String getRole(Long id) {
        Member member;
        try {
            member = memberService.findById(id);
        } catch (DoesNotExistEntityException e) {
            throw new AuthenticationException();
        }

        return member.getRole().name();
    }
}

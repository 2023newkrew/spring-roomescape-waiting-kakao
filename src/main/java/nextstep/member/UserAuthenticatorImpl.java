package nextstep.member;

import auth.UserAuthenticator;
import auth.entity.UserDetails;
import lombok.RequiredArgsConstructor;
import nextstep.exception.MemberException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserAuthenticatorImpl implements UserAuthenticator {
    private final MemberService memberService;

    @Override
    public Optional<UserDetails> authenticate(String username, String password) {
        try {
            Member member = memberService.findByUsernameAndPassword(username, password);
            return Optional.of(new UserDetails(member.getId()));
        } catch (MemberException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isAdmin(Long id) {
        try {
            MemberResponse member = memberService.findById(id);
            return member.getRole() == Role.ADMIN;
        } catch (MemberException e) {
            return false;
        }
    }
}

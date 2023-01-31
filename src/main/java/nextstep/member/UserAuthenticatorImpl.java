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
        Member member;
        try {
            member = memberService.findByUsernameAndPassword(username, password);
        } catch (MemberException e) {
            return Optional.empty();
        }

        return Optional.of(new UserDetails(member.getId(), member.getRole().name()));
    }

    @Override
    public Optional<String> getRole(Long id) {
        Member member;
        try {
            member = memberService.findById(id);
        } catch (MemberException e) {
            return Optional.empty();
        }

        return Optional.of(member.getRole().name());
    }
}

package nextstep.member;

import lombok.RequiredArgsConstructor;
import nextstep.auth.UserDetails;
import nextstep.auth.UserValidator;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserValidatorImpl implements UserValidator {
    private final MemberDao memberDao;

    @Override
    public UserDetails validate(String username, String password) {
        Member member = memberDao.findByUsername(username);
        if (member == null || member.checkWrongPassword(password)) {
            return null;
        }
        return new UserDetails(member.getId(), member.getRole());
    }
}

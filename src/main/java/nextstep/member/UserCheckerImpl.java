package nextstep.member;

import auth.AuthenticationException;
import auth.TokenRequest;
import auth.UserChecker;
import auth.UserDetails;

public class UserCheckerImpl implements UserChecker {
    private final MemberDao memberDao;

    public UserCheckerImpl(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public UserDetails userCheck(TokenRequest tokenRequest) {
        Member member = memberDao.findByUsername(tokenRequest.getUsername());
        if (member == null || member.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        return new UserDetails(member.getId(), member.getRole());
    }
}

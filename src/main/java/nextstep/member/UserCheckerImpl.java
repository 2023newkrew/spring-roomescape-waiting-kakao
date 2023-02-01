package nextstep.member;

import auth.support.AuthenticationException;
import auth.dto.TokenRequest;
import auth.UserChecker;
import auth.UserDetails;
import nextstep.member.domain.Member;
import nextstep.member.repository.MemberDao;

public class UserCheckerImpl implements UserChecker {
    private final MemberDao memberDao;

    public UserCheckerImpl(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public UserDetails check(TokenRequest tokenRequest) {
        Member member = memberDao.findByUsername(tokenRequest.getUsername());
        if (member == null || member.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        return new UserDetails(member.getId(), member.getRole());
    }
}

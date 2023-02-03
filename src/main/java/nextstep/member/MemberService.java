package nextstep.member;

import auth.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    public Member findById(Long id) {
        if (id == null) {
            throw new AuthenticationException();
        }
        return memberDao.findById(id);
    }
}

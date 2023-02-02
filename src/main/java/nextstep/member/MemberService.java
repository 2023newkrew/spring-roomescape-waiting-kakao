package nextstep.member;

import auth.AuthenticationException;
import auth.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    public Member findById(Long id) {
        return memberDao.findById(id);
    }

    public Member findByUserDetatils(UserDetails userDetails) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }
        return memberDao.findById(userDetails.getId());
    }
}

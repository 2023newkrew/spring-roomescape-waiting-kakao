package nextstep.member.service;

import auth.support.AuthenticationException;
import nextstep.member.domain.Member;
import nextstep.member.dto.MemberRequest;
import nextstep.member.repository.MemberDao;
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

package nextstep.service;

import nextstep.domain.dto.request.MemberRequest;
import nextstep.domain.persist.Member;
import nextstep.repository.MemberDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional
    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberDao.findById(id);
    }
}

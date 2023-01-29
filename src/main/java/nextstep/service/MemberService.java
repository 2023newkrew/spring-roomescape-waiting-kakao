package nextstep.service;

import nextstep.domain.member.Member;
import nextstep.domain.member.MemberDao;
import nextstep.dto.request.MemberRequest;
import nextstep.error.ApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.error.ErrorType.MEMBER_NOT_FOUND;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional
    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberDao.findById(id)
                .orElseThrow(() -> new ApplicationException(MEMBER_NOT_FOUND));
    }
}

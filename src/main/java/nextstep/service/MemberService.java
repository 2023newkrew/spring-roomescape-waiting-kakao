package nextstep.service;

import nextstep.controller.dto.request.MemberRequest;
import nextstep.domain.Member;
import nextstep.repository.MemberDao;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    public Member findById(long id) {
        return memberDao.findById(id);
    }
}

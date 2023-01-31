package nextstep.web.member.service;

import nextstep.web.member.dto.MemberRequest;
import nextstep.web.member.dao.MemberDaoImpl;
import nextstep.web.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDaoImpl memberDao;

    public MemberService(MemberDaoImpl memberDao) {
        this.memberDao = memberDao;
    }

    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    public Member findById(Long id) {
        return memberDao.findById(id);
    }
}

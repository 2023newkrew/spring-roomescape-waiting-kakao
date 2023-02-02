package app.nextstep.service;

import app.nextstep.dao.MemberDao;
import app.nextstep.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long create(Member member) {
        return memberDao.save(member);
    }

    public Member findById(Long id) {
        return memberDao.findById(id);
    }
}

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

    public Long create(String username, String password, String name, String phone, String role) {
        return memberDao.save(username, password, name, phone, role);
    }

    public Member findById(Long id) {
        return memberDao.findById(id);
    }
}

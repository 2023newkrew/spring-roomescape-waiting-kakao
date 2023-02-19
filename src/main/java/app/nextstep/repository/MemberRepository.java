package app.nextstep.repository;

import app.auth.repository.LoginRepository;
import app.nextstep.dao.MemberDao;
import app.nextstep.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository implements LoginRepository {
    private MemberDao memberDao;

    public MemberRepository(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member findById(Long id) {
        return memberDao.findById(id).toMember();
    }
    public Member findByUsername(String username) {
        return memberDao.findByUsername(username).toMember();
    }

    public Long save(Member member) {
        return memberDao.save(
                member.getUsername(),
                member.getPassword(),
                member.getRole(),
                member.getName(),
                member.getPhone());
    }
}

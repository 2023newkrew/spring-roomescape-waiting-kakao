package nextstep.member;

import nextstep.support.NotExistEntityException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    public Member findById(Long id) {
        return memberDao.findById(id)
                .orElseThrow(NotExistEntityException::new);
    }

    public Member findByUsername(String username) {
        return memberDao.findByUsername(username)
                .orElseThrow(NotExistEntityException::new);
    }
}

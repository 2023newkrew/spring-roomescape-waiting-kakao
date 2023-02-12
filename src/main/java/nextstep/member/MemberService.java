package nextstep.member;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    public Optional<Member> findById(Long id) {
        return memberDao.findById(id);
    }
}

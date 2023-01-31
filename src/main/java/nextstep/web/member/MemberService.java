package nextstep.web.member;

import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDaoImpl memberDao;

    public MemberService(MemberDaoImpl memberDao) {
        this.memberDao = memberDao;
    }

    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity().toMemberDetail());
    }

    public Member findById(Long id) {
        return Member.fromMemberDetail(memberDao.findById(id));
    }
}

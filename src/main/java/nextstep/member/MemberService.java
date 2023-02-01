package nextstep.member;

import auth.AuthenticationException;
import auth.LoginRequestEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    public Member findById(Long id) {
        return memberDao.findById(id);
    }

    @EventListener
    public void resolveLogin(LoginRequestEvent loginRequestEvent) {
        Member member = memberDao.findByUsername(loginRequestEvent.getUsername());
        if (member == null || member.checkWrongPassword(loginRequestEvent.getPassword())) {
            throw new AuthenticationException();
        }
        loginRequestEvent.setMemberInformation(member.getId(), member.getRole());
    }
}

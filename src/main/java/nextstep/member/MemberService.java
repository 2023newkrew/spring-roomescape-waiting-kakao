package nextstep.member;

import auth.userauth.UserAuthService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberDao memberDao;
    private final UserAuthService userAuthService;

    public MemberService(MemberDao memberDao, UserAuthService userAuthService) {
        this.memberDao = memberDao;
        this.userAuthService = userAuthService;
        init();
    }

    /**
     *
     */
    private void init(){
        List<Member> memberList = memberDao.findAll();
        for (Member member : memberList){
            userAuthService.addUserAuth(Member.toUserAuth(member));
        }
    }

    public Long create(MemberRequest memberRequest) {
        Member memberWithNoId = memberRequest.toEntity();
        Long id = memberDao.save(memberRequest.toEntity());
        userAuthService.addUserAuth(Member.toUserAuth(
                new Member(id, memberWithNoId.getUsername(), memberWithNoId.getPassword(),
                        memberWithNoId.getName(), memberWithNoId.getPhone(), memberWithNoId.getRole())
        ));
        return id;
    }

    public Member findById(Long id) {
        return memberDao.findById(id);
    }
}

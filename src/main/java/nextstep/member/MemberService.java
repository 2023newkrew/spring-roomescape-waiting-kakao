package nextstep.member;

import nextstep.auth.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    public Member findById(Long id) {
        return memberDao.findById(id);
    }

    public Long getPrincipal(String authorizationHeader){
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(authorizationHeader.split(" ")[1]));
        return id;
    }
}

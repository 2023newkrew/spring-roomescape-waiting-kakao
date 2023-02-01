package nextstep.member;

import auth.UserDetails;
import auth.UserDetailsService;
import lombok.RequiredArgsConstructor;
import nextstep.exception.dataaccess.DataAccessErrorCode;
import nextstep.exception.dataaccess.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberDao memberDao;

    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    @Override
    public Member findById(Long id) {
        return memberDao.findById(id)
                .orElseThrow(() -> new DataAccessException(DataAccessErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public UserDetails findByUsername(String username) {
        return memberDao.findByUsername(username)
                .orElseThrow(() -> new DataAccessException(DataAccessErrorCode.MEMBER_NOT_FOUND));
    }
}

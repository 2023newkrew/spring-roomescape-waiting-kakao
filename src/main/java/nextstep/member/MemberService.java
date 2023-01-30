package nextstep.member;

import lombok.RequiredArgsConstructor;
import nextstep.support.DoesNotExistEntityException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberDao memberDao;

    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    public Member findById(Long id) {
        return memberDao.findById(id).orElseThrow(DoesNotExistEntityException::new);
    }
}

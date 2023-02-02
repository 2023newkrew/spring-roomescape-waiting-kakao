package nextstep.service;

import lombok.RequiredArgsConstructor;
import nextstep.domain.dto.request.MemberRequest;
import nextstep.domain.persist.Member;
import nextstep.repository.MemberDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberDao memberDao;

    @Transactional
    public Long addMember(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }
    
}

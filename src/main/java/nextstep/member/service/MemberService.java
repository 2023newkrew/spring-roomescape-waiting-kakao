package nextstep.member.service;

import lombok.RequiredArgsConstructor;
import nextstep.exception.DuplicateEntityException;
import nextstep.exception.NotExistEntityException;
import nextstep.member.dao.MemberDao;
import nextstep.member.model.Member;
import nextstep.member.model.MemberRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberDao memberDao;

    @Transactional
    public Long create(MemberRequest memberRequest) {
        if (memberNameExist(memberRequest)) {
            throw new DuplicateEntityException();
        }
        return memberDao.save(memberRequest.toEntity());
    }

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberDao.findById(id)
                .orElseThrow(NotExistEntityException::new);
    }

    @Transactional(readOnly = true)
    public Member findByMemberName(String name) {
        return memberDao.findByMemberName(name)
                .orElseThrow(NotExistEntityException::new);
    }

    private boolean memberNameExist(MemberRequest memberRequest) {
        try {
            findByMemberName(memberRequest.getMemberName());
        } catch (NotExistEntityException err) {
            return false;
        }

        return true;
    }
}

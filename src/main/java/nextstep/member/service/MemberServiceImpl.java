package nextstep.member.service;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.member.exception.MemberErrorMessage;
import nextstep.member.exception.MemberException;
import nextstep.member.repository.MemberRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository repository;

    @Transactional
    @Override
    public Member create(Member member) {
        return tryInsert(member);
    }

    private Member tryInsert(Member member) {
        try {
            return repository.insert(member);
        }
        catch (DataIntegrityViolationException ignore) {
            throw new MemberException(MemberErrorMessage.CONFLICT);
        }
    }

    @Override
    public Member getById(Long id) {
        return repository.getById(id);
    }
}

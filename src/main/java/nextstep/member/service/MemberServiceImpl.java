package nextstep.member.service;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.MemberEntity;
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
    public MemberEntity create(MemberEntity member) {
        return tryInsert(member);
    }

    private MemberEntity tryInsert(MemberEntity member) {
        try {
            return repository.insert(member);
        }
        catch (DataIntegrityViolationException ignore) {
            throw new MemberException(MemberErrorMessage.CONFLICT);
        }
    }

    @Override
    public MemberEntity getById(Long id) {
        return repository.getById(id);
    }
}

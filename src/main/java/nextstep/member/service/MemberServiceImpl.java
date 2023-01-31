package nextstep.member.service;

import auth.domain.UserRole;
import lombok.RequiredArgsConstructor;
import nextstep.exception.MemberException;
import nextstep.exception.message.ErrorMessage;
import nextstep.member.domain.Member;
import nextstep.member.dto.MemberRequest;
import nextstep.member.dto.MemberResponse;
import nextstep.member.mapper.MemberMapper;
import nextstep.member.repository.MemberRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository repository;

    private final MemberMapper mapper;

    @Transactional
    @Override
    public MemberResponse create(MemberRequest request) {
        Member member = mapper.fromRequest(request, UserRole.NORMAL);

        return mapper.toResponse(tryInsert(member));
    }

    private Member tryInsert(Member member) {
        try {
            return repository.insert(member);
        }
        catch (DataIntegrityViolationException ignore) {
            throw new MemberException(ErrorMessage.MEMBER_CONFLICT);
        }
    }

    @Override
    public MemberResponse getById(Long id) {
        return mapper.toResponse(repository.getById(id));
    }
}

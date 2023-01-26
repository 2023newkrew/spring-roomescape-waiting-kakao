package nextstep.member.service;

import auth.domain.UserRole;
import auth.dto.UserDetailsResponse;
import lombok.RequiredArgsConstructor;
import nextstep.etc.exception.ErrorMessage;
import nextstep.etc.exception.MemberException;
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

    @Override
    public UserDetailsResponse getByUsername(String username) {
        return mapper.toResponse(repository.getByUsername(username));
    }
}

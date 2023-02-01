package com.nextstep.domains.member;

import com.authorizationserver.domains.authorization.enums.RoleType;
import com.nextstep.interfaces.member.dtos.MemberRequest;
import com.nextstep.interfaces.member.dtos.MemberResponse;
import com.nextstep.interfaces.member.dtos.MemberMapper;
import lombok.RequiredArgsConstructor;
import com.nextstep.domains.exceptions.ErrorMessageType;
import com.nextstep.domains.exceptions.MemberException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService{

    private final MemberRepository repository;

    private final MemberMapper mapper;

    @Transactional
    public MemberResponse create(MemberRequest request) {
        Member member = mapper.fromRequest(request, RoleType.NORMAL);

        return mapper.toResponse(tryInsert(member));
    }

    private Member tryInsert(Member member) {
        try {
            return repository.insert(member);
        }
        catch (DataIntegrityViolationException ignore) {
            throw new MemberException(ErrorMessageType.MEMBER_CONFLICT);
        }
    }

    public MemberResponse getById(Long id) {
        return mapper.toResponse(repository.getById(id));
    }

    public MemberResponse getByUsername(String username) {
        return mapper.toResponse(repository.getByUsername(username));
    }
}

package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.MemberControllerPostBody;
import roomescape.entity.Member;
import roomescape.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository repository;

    public long createMember(MemberControllerPostBody request) {
        return repository.insert(request.getUsername(), request.getPassword(), request.getName(), request.getPhone());
    }

    @Transactional(readOnly = true)
    public Member me(long memberId) {
        return repository.selectById(memberId);
    }
}

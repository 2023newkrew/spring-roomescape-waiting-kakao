package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.MemberControllerPostBody;
import roomescape.entity.Member;
import roomescape.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository repository;


    @Transactional
    public long createMember(MemberControllerPostBody request) {
        return repository.insert(request.getUsername(), request.getPassword(), request.getName(), request.getPhone());
    }


    public Member me(long memberId) {
        return repository.selectById(memberId);
    }
}

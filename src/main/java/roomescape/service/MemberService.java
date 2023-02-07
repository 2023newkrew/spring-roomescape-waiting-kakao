package roomescape.service;

import auth.service.UserAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.MemberControllerPostBody;
import roomescape.entity.Member;
import roomescape.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserAuthentication {
    private final MemberRepository repository;

    public long createMember(MemberControllerPostBody request) {
        return repository.insert(request.getUsername(), request.getPassword(), request.getName(), request.getPhone());
    }

    @Transactional(readOnly = true)
    public Member me(long memberId) {
        return repository.selectById(memberId);
    }

    @Override
    public Optional<User> getUser(String username) {
        var user = repository.selectByUsername(username);
        return user.map(v ->
                new UserAuthentication.User(v.getId().toString(), v.getUsername(), v.getPassword(), v.getIsAdmin())
        );
    }
}

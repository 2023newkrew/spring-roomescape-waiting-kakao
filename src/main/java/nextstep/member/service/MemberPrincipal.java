package nextstep.member.service;

import auth.domain.UserDetails;
import auth.service.AuthenticationPrincipal;
import lombok.RequiredArgsConstructor;
import nextstep.member.repository.MemberRepository;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberPrincipal implements AuthenticationPrincipal {

    private final MemberRepository repository;

    @Override
    public UserDetails getByUsername(String username) {
        return repository.getByUsername(username);
    }
}

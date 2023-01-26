package nextstep.member.service;

import auth.domain.UserDetails;
import auth.service.UserDetailsPrincipal;
import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.member.repository.MemberRepository;

@RequiredArgsConstructor
public class MemberPrincipal implements UserDetailsPrincipal {

    private final MemberRepository repository;

    @Override
    public UserDetails getByUsername(String username) {
        Member member = repository.getByUsername(username);
        return new UserDetails(member.getId(), member.getUsername(), member.getPassword(), member.getRole());
    }
}

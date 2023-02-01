package com.nextstep.domains.member;

import com.authorizationserver.domains.authorization.entities.UserDetailsEntity;
import com.authorizationserver.domains.authorization.AuthRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberPrincipal implements AuthRepository {

    private final MemberRepository repository;

    @Override
    public UserDetailsEntity getByUsername(String username) {
        Member member = repository.getByUsername(username);
        return new UserDetailsEntity(member.getId(), member.getUsername(), member.getPassword(), member.getRole());
    }
}

package nextstep.member.repository;

import nextstep.member.domain.MemberEntity;

public interface MemberRepository {

    MemberEntity insert(MemberEntity member);

    MemberEntity getById(Long id);

    MemberEntity getByUsername(String username);
}

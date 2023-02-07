package nextstep.member.repository;

import nextstep.member.domain.Member;

public interface MemberRepository {

    Member insert(Member member);

    Member getById(Long id);

    Member getByUsername(String username);
}

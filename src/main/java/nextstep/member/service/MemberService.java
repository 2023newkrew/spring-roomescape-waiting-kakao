package nextstep.member.service;

import nextstep.member.domain.Member;

public interface MemberService {

    Member create(Member member);

    Member getById(Long id);
}

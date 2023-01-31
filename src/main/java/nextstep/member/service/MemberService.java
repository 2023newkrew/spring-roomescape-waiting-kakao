package nextstep.member.service;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberEntity;

public interface MemberService {

    MemberEntity create(Member member);

    MemberEntity getById(Long id);
}

package nextstep.member.service;

import nextstep.member.domain.MemberEntity;

public interface MemberService {

    MemberEntity create(MemberEntity member);

    MemberEntity getById(Long id);
}

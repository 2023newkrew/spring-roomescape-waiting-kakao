package nextstep.member.service;

import nextstep.member.dto.MemberRequest;
import nextstep.member.dto.MemberResponse;

public interface MemberService {

    MemberResponse create(MemberRequest request);

    MemberResponse getById(Long id);

    MemberResponse getByUsername(String username);
}

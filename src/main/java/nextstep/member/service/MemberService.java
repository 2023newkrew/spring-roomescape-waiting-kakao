package nextstep.member.service;

import auth.service.UserDetailsService;
import nextstep.member.dto.MemberRequest;
import nextstep.member.dto.MemberResponse;

public interface MemberService extends UserDetailsService {

    MemberResponse create(MemberRequest request);

    MemberResponse getById(Long id);
}

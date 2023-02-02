package auth;

import nextstep.member.Member;

public interface MemberDao {
    Long save(Member member);
    MemberDetail findById(Long id);
    MemberDetail findByUsername(String username);
}

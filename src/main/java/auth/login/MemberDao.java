package auth.login;

import nextstep.web.member.Member;

public interface MemberDao {
    Long save(Member member);
    MemberDetail findById(Long id);
    MemberDetail findByUsername(String username);
}

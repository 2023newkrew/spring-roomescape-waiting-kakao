package nextstep.member;

import auth.config.LoginMemberDao;

public interface MemberDao extends LoginMemberDao {
    Long save(Member member);

    Member findById(Long id);

    Member findByUsername(String username);
}

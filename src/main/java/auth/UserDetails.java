package auth;

import nextstep.member.Member;
import nextstep.member.MemberDao;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDetails {
    private MemberDao memberDao;

    public UserDetails() {
        this.memberDao = new MemberDao(new JdbcTemplate());
    }

    public Member findById(Long id) {
        return memberDao.findById(id);
    }
}

package auth;


import nextstep.member.Member;

public interface UserDetailsDao {
    Member findById(Long id);
    Member findByUsername(String username);
}

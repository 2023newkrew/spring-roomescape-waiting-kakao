package auth;


import nextstep.member.Member;

public interface UserDetailsDao {
    UserDetails findUserDetailsById(Long id);
    UserDetails findUserDetailsByUsername(String username);
}

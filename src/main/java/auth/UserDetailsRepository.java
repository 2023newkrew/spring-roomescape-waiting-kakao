package auth;

import nextstep.member.Member;

public interface UserDetailsRepository {
    UserDetails findById(Long id);

    UserDetails findByUsername(String username);
}

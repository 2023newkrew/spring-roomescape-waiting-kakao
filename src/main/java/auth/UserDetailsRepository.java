package auth;

import org.springframework.stereotype.Repository;

/**
 * MemberDao에 대한 의존 제거하기 위해 인터페이스로 생성
 */
@Repository
public interface UserDetailsRepository {
    UserDetails findByUsername(String username);
}

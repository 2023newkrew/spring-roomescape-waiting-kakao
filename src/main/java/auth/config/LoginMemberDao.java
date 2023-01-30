package auth.config;

public interface LoginMemberDao {
    MemberDetails findById(Long id);
    MemberDetails findByUsername(String username);
}

package auth;

public interface LoginMemberDao {
    MemberDetails findById(Long id);
    MemberDetails findByUsername(String username);
}

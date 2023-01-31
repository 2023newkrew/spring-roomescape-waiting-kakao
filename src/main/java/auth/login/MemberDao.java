package auth.login;

public interface MemberDao {
    Long save(MemberDetail member);
    MemberDetail findById(Long id);
    MemberDetail findByUsername(String username);
}

package auth.login;

public interface MemberDao<T extends AbstractMember> {
    Long save(T entity);
    T findById(Long id);
    T findByUsername(String username);
}

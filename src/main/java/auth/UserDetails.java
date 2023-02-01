package auth;

public class UserDetails {
    private final Long id;

    public UserDetails(Long id) {
        validateId(id);
        this.id = id;
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new AuthenticationException();
        }
    }

    public Long getId() {
        return id;
    }
}

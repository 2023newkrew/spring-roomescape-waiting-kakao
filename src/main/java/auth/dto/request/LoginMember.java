package auth.dto.request;

public class LoginMember {

    private Long id;

    public LoginMember(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

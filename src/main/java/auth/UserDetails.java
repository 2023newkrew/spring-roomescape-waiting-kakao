package auth;

/**
 * Username, Password 등 사용자 로그인 정보를 저장하는 클래스
 */
public class UserDetails {
    private final Long id;
    private final String username;
    private final String password;
    private final String role;

    public UserDetails(final Long id, final String username, final String password, final String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public boolean checkWrongPassword(String password){
        return !this.password.equals(password);
    }

    public Long getId(){
        return id;
    }

    public String getRole(){
        return role;
    }
}

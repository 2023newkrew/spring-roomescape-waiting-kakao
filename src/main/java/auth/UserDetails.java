package auth;

/**
 * Username, Password 등 사용자 로그인 정보를 저장하는 클래스
 * Member에 대한 의존을 제거하기 위해 생성
 */
public class UserDetails {
    private final Long id;
    private final String username;
    private final String password;
    private final String name;
    private final String phone;
    private final String role;

    public UserDetails(final Long id, final String username, final String password, final String name, final String phone, final String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public boolean checkWrongPassword(String password){
        return !this.password.equals(password);
    }
}

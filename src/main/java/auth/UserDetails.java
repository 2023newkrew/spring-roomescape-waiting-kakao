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

    public static  UserDetailsBuilder builder() {
        return new UserDetailsBuilder();
    }
    public static class UserDetailsBuilder {
        private Long id;
        private String username;
        private String password;
        private String name;
        private String phone;
        private String role;

        private UserDetailsBuilder() {
        }

        public UserDetailsBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserDetailsBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserDetailsBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserDetailsBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserDetailsBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserDetailsBuilder role(String role) {
            this.role = role;
            return this;
        }

        public UserDetails build() {
            return new UserDetails(id, username, password, name, phone, role);
        }
    }
}

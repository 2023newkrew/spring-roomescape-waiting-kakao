package nextstep.member;

import auth.UserDetails;

public class Member {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    /* RestAssured에서 사용 */
    @SuppressWarnings("unused")
    public Member() {
    }

    public Member(Long id, String username, String password, String name, String phone, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public Member(String username, String password, String name, String phone, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public Member(UserDetails userDetails) {
        this(userDetails.getId(), userDetails.getUsername(), userDetails.getPassword(),
                userDetails.getName(), userDetails.getPhone(), userDetails.getRole());
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

    public static MemberBuilder builder() {
        return new MemberBuilder();
    }

    public static class MemberBuilder {
        private Long id;
        private String username;
        private String password;
        private String name;
        private String phone;
        private String role;

        private MemberBuilder() {
        }

        public MemberBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MemberBuilder username(String username) {
            this.username = username;
            return this;
        }

        public MemberBuilder password(String password) {
            this.password = password;
            return this;
        }

        public MemberBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MemberBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public MemberBuilder role(String role) {
            this.role = role;
            return this;
        }

        public Member build() {
            return new Member(id, username, password, name, phone, role);
        }
    }
}

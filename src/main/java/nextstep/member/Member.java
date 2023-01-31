package nextstep.member;

import static nextstep.utils.Validator.checkFieldIsNull;

import auth.config.MemberDetails;

public class Member implements MemberDetails {
    private Long id;
    private final String username;
    private final String password;
    private final String name;
    private final String phone;
    private final String role;

    private Member(String username, String password, String name, String phone, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
        validateFields();
    }

    public static Member giveId(Member member, Long id) {
        checkFieldIsNull(member, "member");
        checkFieldIsNull(id, "id");
        member.id = id;
        return member;
    }

    public static MemberBuilder builder(){
        return new MemberBuilder();
    }

    public static class MemberBuilder {
        private String username;
        private String password;
        private String name;
        private String phone;
        private String role;

        public MemberBuilder username(String username){
            this.username = username;
            return this;
        }
        public MemberBuilder password(String password){
            this.password = password;
            return this;
        }
        public MemberBuilder name(String name){
            this.name = name;
            return this;
        }
        public MemberBuilder phone(String phone){
            this.phone = phone;
            return this;
        }
        public MemberBuilder role(String role){
            this.role = role;
            return this;
        }
        public Member build(){
            return new Member(username, password, name, phone, role);
        }
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

    public boolean checkWrongPassword(String password) {
        return !this.password.equals(password);
    }
    private void validateFields() {
        checkFieldIsNull(username, "username");
        checkFieldIsNull(password, "password");
        checkFieldIsNull(name, "name");
        checkFieldIsNull(phone, "phone");
        checkFieldIsNull(role, "role");
    }
}

package nextstep.member;

import auth.userauth.UserAuth;

public class Member {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

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

    public Member(UserAuth userAuth) {
        this(userAuth.getId(), userAuth.getUsername(), userAuth.getPassword(),
                userAuth.getName(), userAuth.getPhone(), userAuth.getRole());
    }

    public static UserAuth toUserAuth(Member member){
        return new UserAuth(member.getId(), member.getUsername(), member.getPassword(),
                member.getName(), member.getPhone(), member.getRole());
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

//    public boolean checkWrongPassword(String password) {
//        return !this.password.equals(password);
//    }
}

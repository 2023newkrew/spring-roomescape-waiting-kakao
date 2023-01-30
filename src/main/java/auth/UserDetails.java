package auth;

import nextstep.member.Member;

import java.util.List;

/**
 * Username, Password 등 사용자 로그인 정보를 저장하는 클래스
 */
public class UserDetails {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    public UserDetails(final Long id, final String username, final String password, final String name, final String phone, final String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public UserDetails(Member member){
        this(member.getId(), member.getUsername(), member.getPassword(),
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

    public boolean checkWrongPassword(String password){
        return !this.password.equals(password);
    }
}

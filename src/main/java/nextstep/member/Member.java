package nextstep.member;

import auth.UserDetails;

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

    public UserDetails toUserDetails() {
        return new UserDetails(id, username, password, name, phone, role);
    }

    public static Member of(UserDetails userDetails) {
        return new Member(userDetails.getId(), userDetails.getName(), userDetails.getPassword(), userDetails.getName(), userDetails.getPhone(),
                userDetails.getRole());
    }
}

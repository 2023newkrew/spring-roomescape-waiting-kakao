package auth.domain;

import nextstep.member.Role;

public class UserDetails {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private Role role;

    public UserDetails() {
    }

    public UserDetails(Long id, String username, String password, String name, String phone, Role role) {
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

    public Role getRole() {
        return role;
    }

    public boolean checkWrongPassword(String password) {
        return !this.password.equals(password);
    }
}

package nextstep.domain.persist;

import auth.domain.persist.UserDetails;

public class Member {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    public Member() {
    }

    public Member(UserDetails userDetails) {
        this.id = userDetails.getId();
        this.username = userDetails.getUsername();
        this.password = userDetails.getPassword();
        this.name = userDetails.getName();
        this.phone = userDetails.getPhone();
        this.role = userDetails.getRole();
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
}

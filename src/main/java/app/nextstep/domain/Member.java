package app.nextstep.domain;

import app.auth.domain.User;

public class Member extends User {
    private String name;
    private String phone;

    public Member() {
    }

    public Member(Long id, String username, String password, String name, String phone, String role) {
        super(id, username, password, role);
        this.name = name;
        this.phone = phone;
    }

    public Member(String username, String password, String name, String phone, String role) {
        super(null, username, password, role);
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}

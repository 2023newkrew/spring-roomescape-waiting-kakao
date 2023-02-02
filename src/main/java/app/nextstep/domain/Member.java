package app.nextstep.domain;

import app.auth.domain.User;

public class Member extends User {
    private String name;
    private String phone;

    public Member() {

    }

    public Member(Long id, String username, String password, String role, String name, String phone) {
        super(id, username, password, role);
        this.name = name;
        this.phone = phone;
    }

    public Member(String username, String password, String role, String name, String phone) {
        super(null, username, password, role);
        this.name = name;
        this.phone = phone;
    }

    public Member(Long id) {
        this(id, null, null, null, null, null);
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}

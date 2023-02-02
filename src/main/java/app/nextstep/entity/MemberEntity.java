package app.nextstep.entity;

import app.nextstep.domain.Member;

public class MemberEntity {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    public MemberEntity() {
    }

    public MemberEntity(Long id, String username, String password, String role, String name, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.phone = phone;
    }

    public Member toMember() {
        return new Member(id, username, password, role, name, phone);
    }
}

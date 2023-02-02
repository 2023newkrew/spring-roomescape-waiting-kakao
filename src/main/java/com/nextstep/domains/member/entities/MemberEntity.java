package com.nextstep.domains.member.entities;

import com.authorizationserver.domains.authorization.entities.UserDetailsEntity;

public class MemberEntity {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    public MemberEntity() {
    }

    public static MemberEntity of(UserDetailsEntity userDetails) {
        return new MemberEntity(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getName(),
                userDetails.getRole(),
                userDetails.getRole()
        );
    }

    public MemberEntity(Long id, String username, String password, String name, String phone, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public MemberEntity(String username, String password, String name, String phone, String role) {
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

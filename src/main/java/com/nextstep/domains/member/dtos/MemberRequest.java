package com.nextstep.domains.member.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextstep.domains.member.entities.MemberEntity;

public class MemberRequest {
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    @JsonCreator
    public MemberRequest(@JsonProperty(value = "username") String username,
                         @JsonProperty(value = "password") String password,
                         @JsonProperty(value = "name") String name,
                         @JsonProperty(value = "phone") String phone,
                         @JsonProperty(value = "role") String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
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

    public MemberEntity toEntity() {
        return new MemberEntity(username, password, name, phone, role);
    }
}

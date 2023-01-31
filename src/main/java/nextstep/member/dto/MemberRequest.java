package nextstep.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nextstep.member.Member;

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

    public Member toEntity() {
        return new Member(username, password, name, phone, role);
    }
}

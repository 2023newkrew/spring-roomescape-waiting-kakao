package com.nextstep.interfaces.member.dtos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class MemberRequest {

    @NotBlank(message = "username은 공백일 수 없습니다.")
    private final String username;

    @NotBlank(message = "password는 공백일 수 없습니다.")
    private final String password;

    @NotBlank(message = "name은 공백일 수 없습니다.")
    private final String name;

    @NotBlank(message = "phone은 공백일 수 없습니다.")
    private final String phone;

    public MemberRequest() {
        this(null, null, null, null);
    }
}

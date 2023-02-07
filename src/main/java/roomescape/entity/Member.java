package roomescape.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @NotNull
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotBlank
    private String name;

    @NotNull
    private String phone;

    @NotNull
    private Boolean isAdmin;
}

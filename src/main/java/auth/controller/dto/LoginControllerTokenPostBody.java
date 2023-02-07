package auth.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginControllerTokenPostBody {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}

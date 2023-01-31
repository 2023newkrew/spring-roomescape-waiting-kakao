package auth.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum Roles {

    ADMIN("ADMIN", 3),
    GUEST("GUEST", 2),
    USER("USER", 1);

    private final String role;
    private final int accessLevel;

    public static Roles of(String string) {
        return Arrays.stream(Roles.values())
                .filter(role -> role.getRole().equals(string))
                .findAny()
                .orElseThrow(NoSuchFieldError::new);
        /* todo: 커스텀익셉션 정의 */
    }
}

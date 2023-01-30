package auth.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum RoleType {
    ADMIN("ROLE_ADMIN"),
    MEMBER("ROLE_MEMBER");

    private final String type;

    RoleType(String type){
        this.type = type;
    }

    public static RoleType from(String type){
        return Arrays.stream(RoleType.values())
                .filter(memberRoleType -> memberRoleType.isSameType(type))
                .findFirst()
                .orElseThrow();
    }

    public static List<RoleType> of(List<String> roles) {
        return roles.stream()
                .map(RoleType::from)
                .collect(Collectors.toList());
    }

    public boolean isSameType(String type) {
        return this.type.equals(type);
    }
}

package auth.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MemberRoleType {
    ADMIN("ROLE_ADMIN"),
    MEMBER("ROLE_MEMBER");

    private final String type;

    MemberRoleType(String type){
        this.type = type;
    }

    public static MemberRoleType from(String type){
        return Arrays.stream(MemberRoleType.values())
                .filter(memberRoleType -> memberRoleType.isSameType(type))
                .findFirst()
                .orElseThrow();
    }

    public static List<MemberRoleType> of(List<String> roles) {
        return roles.stream()
                .map(MemberRoleType::from)
                .collect(Collectors.toList());
    }

    public boolean isSameType(String type) {
        return this.type.equals(type);
    }
}

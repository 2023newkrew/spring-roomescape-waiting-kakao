package auth.domain;

import java.util.Arrays;
import java.util.List;

public class MemberRoles {
    private final List<MemberRoleType> roles;

    public MemberRoles(List<String> roles){
        this.roles = MemberRoleType.of(roles);
    }

    public boolean hasRole(MemberRoleType roleType){
        return roles.contains(roleType);
    }

    public boolean hasRoles(MemberRoleType[] roleTypes){
        return Arrays.stream(roleTypes)
                .allMatch(this::hasRole);
    }
}

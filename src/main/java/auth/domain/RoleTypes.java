package auth.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoleTypes {
    private final List<RoleType> roles;

    public RoleTypes(){
        this.roles = new ArrayList<>();
    }
    public RoleTypes(List<RoleType> roles){
        this.roles = new ArrayList<>(roles);
    }

    public boolean hasRoles(RoleType[] roleTypes){
        return Arrays.stream(roleTypes)
                .allMatch(this::hasRole);
    }

    public boolean hasRole(RoleType roleType){
        return roles.contains(roleType);
    }

    public RoleTypes add(RoleTypes roleTypes){
        RoleTypes copied = this.copy();
        copied.roles.addAll(roleTypes.roles);

        return copied;
    }
    public RoleTypes copy(){
        return new RoleTypes(this.roles);
    }
}

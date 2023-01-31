package auth.model;

import auth.domain.RoleTypes;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberDetails {
    private final Long id;
    private final String memberName;

    private final String password;
    private final RoleTypes roles;

    public MemberDetails(Long id, String memberName, String password) {
        this.id = id;
        this.memberName = memberName;
        this.password = password;
        this.roles = new RoleTypes();
    }

    public void addRole(RoleTypes roleTypes){
        roles.add(roleTypes);
    }

    public boolean isValidPassword(String password) {
        return this.password.equals(password);
    }
}
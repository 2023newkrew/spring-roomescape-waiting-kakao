package auth;

public interface MemberDetailsService {

    MemberDetails loadMemberDetailsByUsername(String username);
}
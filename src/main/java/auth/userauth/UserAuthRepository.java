package auth.userauth;

import java.util.HashMap;
import java.util.Map;

/**
 * UserAuth에 관련된 정보를 관리하는 Repository 클래스 <br><br>
 * 현재 DB를 따로 만들지는 않고 Map으로 구현하였으나,<br>
 * 만약 실제 프로그램을 구현한다면 DB를 따로 구축하는 것도 고려해야 한다고 생각한다.
 */
public class UserAuthRepository {
    private final Map<String, UserAuth> userListByUsername = new HashMap<>();
    private final Map<Long, UserAuth> userListById = new HashMap<>();
    public void addUserAuth(UserAuth userAuth){
        userListByUsername.put(userAuth.getUsername(), userAuth);
        userListById.put(userAuth.getId(), userAuth);
    }

    public UserAuth findByUsername(String username){
        return userListByUsername.get(username);
    }
    public UserAuth findById(Long memberId){
        return userListById.get(memberId);
    }
}

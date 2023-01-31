package auth.userauth;

import java.util.HashMap;
import java.util.Map;

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

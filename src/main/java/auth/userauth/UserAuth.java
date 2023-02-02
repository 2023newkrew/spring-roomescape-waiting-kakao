package auth.userauth;

/**
 * <b>"요구사항 1 - auth package에서 nextstep(MemberDao 및 Member)를 의존하지 말 것"</b> 에 따라,<br>
 * 인증을 위한 데이터를 auth 패키지 내에서 해결하기 위해 생성한 Entity 클래스. <br>
 * <br>
 *
 * 기존 코딩에서는 username, password만을 가져오는 것으로 구현하였으나 <br>
 * 확장성(추후 name, phone 등도 인증정보에 포함시키는 경우)을 대비해서 <br>
 * step 2 기준 Member Entity와 동일한 필드를 가지도록 현재 구현하였다. <br>
 */
public class UserAuth {
    private final Long id;
    private final String username;
    private final String password;
    private final String name;
    private final String phone;
    private final String role;

    public UserAuth(final Long id, final String username, final String password, final String name, final String phone, final String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }


    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public boolean checkWrongPassword(String password){
        return !this.password.equals(password);
    }
}

package nextstep.member;

public class MemberRequest {
    private final String username;
    private final String password;
    private final String name;
    private final String phone;
    private final String role;

    public MemberRequest(String username, String password, String name, String phone, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
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

    /* RestAssured에서 사용 */
    @SuppressWarnings("unused")
    public String getPhone() {
        return phone;
    }

    /* RestAssured에서 사용 */
    @SuppressWarnings("unused")
    public String getRole() {
        return role;
    }

    public Member toEntity() {
        return new Member(username, password, name, phone, role);
    }
}

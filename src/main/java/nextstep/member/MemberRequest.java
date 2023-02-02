package nextstep.member;

public record MemberRequest(String username, String password, String name, String phone, String role) {

    /* RestAssured에서 사용 */
    @Override
    @SuppressWarnings("unused")
    public String phone() {
        return phone;
    }

    /* RestAssured에서 사용 */
    @Override
    @SuppressWarnings("unused")
    public String role() {
        return role;
    }

    public Member toEntity() {
        return new Member(username, password, name, phone, role);
    }
}

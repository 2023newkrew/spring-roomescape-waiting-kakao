package roomescape.nextstep.member;

public record MemberRequest(String username, String password, String name, String phone, String role) {
    public Member toEntity() {
        return new Member(username, password, name, phone, role);
    }
}
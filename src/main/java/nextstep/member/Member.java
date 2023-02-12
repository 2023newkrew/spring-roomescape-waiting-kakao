package nextstep.member;

import auth.domain.UserDetails;
import java.util.Optional;
import nextstep.error.ErrorCode;
import nextstep.error.exception.RoomReservationException;

public class Member {
    private Optional<Long> id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private Role role;

    public Member() {
    }

    public Member(Long id, String username, String password, String name, String phone, Role role) {
        this.id = Optional.ofNullable(id);
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public Member(String username, String password, String name, String phone, Role role) {
        this(null, username, password, name, phone, role);
    }

    public Member(UserDetails userDetails) {
        this(userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getName(),
                userDetails.getPhone(),
                userDetails.getRole()
        );
    }

    public Optional<Long> getId() {
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

    public Role getRole() {
        return role;
    }

    public UserDetails convertToUserDetails() {
        return new UserDetails(id.orElseThrow(() -> {
            throw new RoomReservationException(ErrorCode.INVALID_MEMBER);
        }), username, password, name, phone, role);
    }
}

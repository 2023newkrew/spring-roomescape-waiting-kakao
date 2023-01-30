package nextstep.member.dto;

import nextstep.member.Member;

public class MemberResponse {
    private final Long id;
    private final String username;
    private final String password;
    private final String name;
    private final String phone;
    private final String role;

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

    protected MemberResponse(Long id, String username, String password, String name, String phone, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public static MemberResponseBuilder builder() {
        return new MemberResponseBuilder();
    }

    public static MemberResponse of(Member member) {
        return builder()
                .username(member.getUsername())
                .id(member.getId())
                .password(member.getPassword())
                .name(member.getName())
                .phone(member.getPhone())
                .role(member.getRole())
                .build();
    }

    public static class MemberResponseBuilder {
        private Long id;
        private String username;
        private String name;
        private String phone;
        private String role;
        private String password;

        public MemberResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MemberResponseBuilder username(String username) {
            this.username = username;
            return this;
        }

        public MemberResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MemberResponseBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public MemberResponseBuilder role(String role) {
            this.role = role;
            return this;
        }

        public MemberResponseBuilder password(String password) {
            this.password = password;
            return this;
        }

        public MemberResponse build() {
            return new MemberResponse(this.id, this.username, this.password, this.name, this.phone, this.role);
        }

    }
}

package roomescape.command;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import roomescape.repository.MemberRepository;

import java.util.Optional;


@Component
@Profile("console")
@CommandLine.Command(name = "auth", description = "로그인 관련 API 입니다.")
public class AuthCommand {
    private final MemberRepository repository;
    private Long id = null;

    public AuthCommand(MemberRepository repository) {
        this.repository = repository;
    }

    @CommandLine.Command(name = "login", description = "로그인을 시도합니다. ex) auth login 아이디 패스워드")
    public void login(
            @CommandLine.Parameters(index = "0") String username,
            @CommandLine.Parameters(index = "1") String password
    ) {
        if (id != null) {
            System.out.println("이미 로그인된 상황입니다.");
            return;
        }
        var member = repository.selectByUsername(username);
        if (!member.getPassword().equals(password)) {
            System.out.println("아이디, 혹은 비밀번호가 일치하지 않습니다.");
            return;
        }
        System.out.println("로그인에 성공했습니다.");
        this.id = member.getId();
    }

    @CommandLine.Command(name = "logout", description = "로그아웃 합니다. ex) auth logout")
    public void logout() {
        if (id == null) {
            System.out.println("로그인 정보가 없습니다.");
            return;
        }
        this.id = null;
    }

    public Optional<Long> getCurrentMemberId() {
        return Optional.ofNullable(this.id);
    }
}

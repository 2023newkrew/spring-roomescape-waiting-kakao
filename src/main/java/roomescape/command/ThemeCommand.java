package roomescape.command;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import roomescape.repository.ThemeRepository;


@Component
@Profile("console")
@CommandLine.Command(name = "theme", description = "테마를 추가/삭제/조회 할 수 있는 명령어입니다.")
@RequiredArgsConstructor
public class ThemeCommand {
    private final ThemeRepository themeRepository;
    
    @CommandLine.Command(name = "add", description = "테마를 추가합니다. ex) theme add 새테마 새로운테마입니다. 1234000")
    public void add(
            @CommandLine.Parameters(index = "0") String name,
            @CommandLine.Parameters(index = "1") String desc,
            @CommandLine.Parameters(index = "2") Integer price
    ) {
        var id = themeRepository.insert(name, desc, price);
        System.out.println("테마가 생성되었습니다.");
        System.out.printf("생성된 id : %d\n", id);
    }

    @CommandLine.Command(name = "find", description = "테마를 찾습니다. ex) theme find 1")
    public void find(
            @CommandLine.Parameters(index = "0") Long id
    ) {
        var foundTheme = themeRepository.selectById(id);
        foundTheme.ifPresentOrElse(
                (theme) -> {
                    System.out.println("테마를 찾았습니다.");
                    System.out.printf("테마 이름 : %s\n", theme.getName());
                    System.out.printf("테마 설명 : %s\n", theme.getDesc());
                    System.out.printf("테마 가격 : %d\n", theme.getPrice());
                },
                () -> {
                    System.out.println("해당 아이디의 테마가 존재하지 않습니다.");
                    System.out.printf("검색한 id : %d\n", id);
                }
        );
    }

    @CommandLine.Command(name = "delete", description = "테마를 삭제합니다. ex) theme delete 1")
    public void delete(
            @CommandLine.Parameters(index = "0") Long id
    ) {
        var affectedRows = themeRepository.delete(id);
        if (affectedRows == 0) {
            System.out.println("테마를 삭제할 수 없습니다.");
            System.out.println("사유 : 사용중인 테마는 중간에 삭제 불가능");
            System.out.printf("삭제 시도한 id : %d\n", id);
            return;
        }
        System.out.println("테마가 삭제되었습니다.");
        System.out.printf("삭제된 id = %d\n", id);
    }

    @CommandLine.Command(name = "help", description = "사용 가능한 도움말을 봅니다.")
    public void help() {
        new CommandLine(this).usage(System.out);
    }

}

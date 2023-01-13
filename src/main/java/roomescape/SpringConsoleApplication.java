package roomescape;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Profile;
import roomescape.command.Command;

import java.util.Scanner;

@SpringBootApplication
@Profile("console")
public class SpringConsoleApplication implements CommandLineRunner {

    private final Command command;

    public SpringConsoleApplication(Command command) {
        this.command = command;
    }


    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringConsoleApplication.class)
                .profiles("console")
                .web(WebApplicationType.NONE)
                .run(args);
    }


    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("help를 통해 사용 가능한 명령어를 볼 수 있습니다.");
        System.out.println("ex) help");
        System.out.println("ex) theme help");
        System.out.println("ex) theme add help");
        System.out.println("ex) reservation add help");
        System.out.println("ex) ...");
        while (true) {
//            printHelp();
            var input = scanner.nextLine().split(" +");
            command.command().execute(input);
        }
    }
}

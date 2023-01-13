package roomescape.command;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@Profile("console")
@CommandLine.Command(name = "roomescape")
public class Command {
    private final CommandLine commandLine;

    public Command(ThemeCommand themeCommand, ReservationCommand reservationCommand) {
        commandLine = new CommandLine(this)
                .addSubcommand(themeCommand)
                .addSubcommand(reservationCommand)
        ;
    }

    @CommandLine.Command(name = "help", description = "사용 가능한 도움말을 봅니다.")
    public void help() {
        commandLine.usage(System.out);
    }

    public CommandLine command() {
        return commandLine;
    }

}

package roomescape.command;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Component
@Profile("console")
@CommandLine.Command(name = "reservation", description = "예약을 추가/삭제/조회 할 수 있는 명령어입니다.")
@RequiredArgsConstructor
public class ReservationCommand {
    private final AuthCommand authCommand;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;


    @CommandLine.Command(name = "add", description = "예약을 추가합니다. 형식) <예약자> <예약일> <예약시각> <테마ID>, ex) reservation add 예약자 2023-01-01 13:00 1")
    public void add(
            @CommandLine.Parameters(index = "0") String name,
            @CommandLine.Parameters(index = "1") LocalDate date,
            @CommandLine.Parameters(index = "2") LocalTime time,
            @CommandLine.Parameters(index = "3") Long themeId
    ) {
        var memberId = authCommand.getCurrentMemberId();
        if (memberId.isEmpty()) {
            System.out.println("로그인해야 합니다.");
            return;
        }
        reservationRepository.insert(name, date, time, themeId, memberId.get()).ifPresentOrElse(
                (id) -> {
                    System.out.println("예약이 등록되었습니다.");
                    System.out.println("예약 번호: " + id);
                    System.out.println("예약 날짜: " + date);
                    System.out.println("예약 시간: " + time);
                    System.out.println("예약자 이름: " + name);
                },
                () -> {
                    System.out.println("예약 등록에 실패했습니다.");
                    System.out.println("사유 : 존재하지 않는 테마, 혹은 해당 시간에 예약이 이미 존재");
                }
        );
    }

    @CommandLine.Command(name = "find", description = "예약을 확인합니다. ex) reservation find 1")
    public void find(
            @CommandLine.Parameters(index = "0") Long id
    ) {
        reservationRepository.selectById(id).ifPresentOrElse(
                (reservation) -> {
                    System.out.println("예약을 찾았습니다.");
                    System.out.println("예약 번호: " + reservation.getId());
                    System.out.println("예약 날짜: " + reservation.getDate());
                    System.out.println("예약 시간: " + reservation.getTime());
                    System.out.println("예약자 이름: " + reservation.getName());
                    System.out.println("예약 테마 이름: " + reservation.getTheme().getName());
                    System.out.println("예약 테마 설명: " + reservation.getTheme().getDesc());
                    System.out.println("예약 테마 가격: " + reservation.getTheme().getPrice());
                },
                () -> {
                    System.out.println("해당 아이디의 예약이 존재하지 않습니다.");
                    System.out.println("예약 번호: " + id);
                }
        );
    }

    @CommandLine.Command(name = "delete", description = "예약을 삭제합니다. ex) reservation delete 1")
    public void delete(
            @CommandLine.Parameters(index = "0") Long id
    ) {
        var memberId = authCommand.getCurrentMemberId();
        if (memberId.isEmpty()) {
            System.out.println("로그인해야 합니다.");
            return;
        }
        var reservation = reservationRepository.selectById(id);
        if (reservation.isEmpty()) {
            System.out.println("예약을 취소할 수 없습니다.");
            System.out.println("사유 : 존재하지 않는 ID");
            return;
        }
        if (!Objects.equals(reservation.get().getMemberId(), memberId.get())) {
            System.out.println("예약자만 삭제할 수 있습니다.");
            return;
        }
        reservationRepository.delete(id);
        System.out.println("예약이 취소되었습니다.");
    }

    @CommandLine.Command(name = "help", description = "사용 가능한 도움말을 봅니다.")
    public void help() {
        new CommandLine(this).usage(System.out);
    }
}

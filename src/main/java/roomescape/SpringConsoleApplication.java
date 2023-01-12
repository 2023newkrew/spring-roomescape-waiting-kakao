package roomescape;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Profile;
import roomescape.entity.Reservation;
import roomescape.entity.Theme;
import roomescape.repository.ReservationConsoleRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

@SpringBootApplication
@Profile("console")
public class SpringConsoleApplication implements CommandLineRunner {
    private static final String ADD = "add";
    private static final String FIND = "find";
    private static final String DELETE = "delete";
    private static final String QUIT = "quit";

    private final ReservationConsoleRepository repository;

    public SpringConsoleApplication(ReservationConsoleRepository repository) {
        this.repository = repository;
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

        Theme theme = new Theme(null, "워너고홈", "병맛 어드벤처 회사 코믹물", 29_000);

        while (true) {
            System.out.println();
            System.out.println("### 명령어를 입력하세요. ###");
            System.out.println("- 예약하기: add {date},{time},{name} ex) add 2022-08-11,13:00,류성현");
            System.out.println("- 예약조회: find {id} ex) find 1");
            System.out.println("- 예약취소: delete {id} ex) delete 1");
            System.out.println("- 종료: quit");

            String input = scanner.nextLine();
            if (input.startsWith(ADD)) {
                String params = input.split(" ")[1];

                String date = params.split(",")[0];
                String time = params.split(",")[1];
                String name = params.split(",")[2];

                Reservation reservation = new Reservation(null, LocalDate.parse(date),
                        LocalTime.parse(time + ":00"), name, theme);
                var newReservationId = repository.addReservation(reservation);
                if (newReservationId.isEmpty()) {
                    System.out.println("이미 해당 시간에 예약내역이 존재합니다");
                    continue;
                }
                System.out.println("예약이 등록되었습니다.");
                System.out.println("예약 번호: " + newReservationId.get());
                System.out.println("예약 날짜: " + reservation.getDate());
                System.out.println("예약 시간: " + reservation.getTime());
                System.out.println("예약자 이름: " + reservation.getName());
            }

            if (input.startsWith(FIND)) {
                String params = input.split(" ")[1];

                Long id = Long.parseLong(params.split(",")[0]);

                var reservation = repository.findReservation(id);
                if (reservation.isEmpty()) {
                    System.out.println("예약 번호: " + id + "는 존재하지 않는 예약 번호입니다.");
                    continue;
                }
                System.out.println("예약 번호: " + reservation.get().getId());
                System.out.println("예약 날짜: " + reservation.get().getDate());
                System.out.println("예약 시간: " + reservation.get().getTime());
                System.out.println("예약자 이름: " + reservation.get().getName());
                System.out.println("예약 테마 이름: " + reservation.get().getTheme().getName());
                System.out.println("예약 테마 설명: " + reservation.get().getTheme().getDesc());
                System.out.println("예약 테마 가격: " + reservation.get().getTheme().getPrice());
            }

            if (input.startsWith(DELETE)) {
                String params = input.split(" ")[1];

                Long id = Long.parseLong(params.split(",")[0]);

                repository.deleteReservation(id);
                System.out.println("예약이 취소되었습니다.");
            }

            if (input.equals(QUIT)) {
                break;
            }
        }
    }
}

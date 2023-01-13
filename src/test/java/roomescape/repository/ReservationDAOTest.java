package roomescape.repository;

import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
public class ReservationDAOTest {
//
//    private static final String ADD_SQL = "INSERT INTO reservation (date, time, name, theme_name, theme_desc, theme_price) VALUES (?, ?, ?, ?, ?, ?);";
//
//    private static final LocalDate DATE = LocalDate.parse("2022-08-01");
//    private static final LocalTime TIME = LocalTime.parse("13:00");
//    private static final String NAME = "test";
//    private static final String THEME_NAME = "워너고홈";
//    private static final String THEME_DESC = "병맛 어드벤처 회사 코믹물";
//    private static final int THEME_PRICE = 29000;
//
//    private ReservationRepository reservationDAO;
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
//
//    @BeforeEach
//    void setUp() {
//        reservationDAO = new ReservationRepository(namedParameterJdbcTemplate);
//
//        jdbcTemplate.execute("DROP TABLE RESERVATION IF EXISTS");
//        jdbcTemplate.execute("CREATE TABLE RESERVATION("
//                + "id bigint not null auto_increment, date date, time time,"
//                + " name varchar(20), theme_name varchar(20), theme_desc  varchar(255),"
//                + " theme_price int, primary key (id));");
//
//        List<Object[]> split = List.<Object[]>of(
//                new Object[]{DATE, TIME, NAME, THEME_NAME, THEME_DESC, THEME_PRICE});
//
//        jdbcTemplate.batchUpdate(ADD_SQL, split);
//    }
//
//    @Test
//    void addReservation() {
//        Reservation reservation = new Reservation(null, LocalDate.parse("2022-08-02"),
//                LocalTime.parse("13:00"), "test", new Theme(null, "워너고홈", "병맛 어드벤처 회사 코믹물", 29000));
//        reservationDAO.addReservation(reservation);
//
//        Long count = jdbcTemplate.queryForObject("SELECT count(*) FROM RESERVATION", Long.class);
//        assertThat(count).isEqualTo(2L);
//    }
//
//    @Test
//    void findReservation() {
//        assertThat(reservationDAO.findReservation(1L))
//                .isPresent()
//                .get().satisfies(reservation -> {
//                    assertThat(reservation.getName()).isEqualTo(NAME);
//                    assertThat(reservation.getDate()).isEqualTo(DATE);
//                    assertThat(reservation.getTime()).isEqualTo(TIME);
//                    assertThat(reservation.getTheme().getName()).isEqualTo(THEME_NAME);
//                    assertThat(reservation.getTheme().getDesc()).isEqualTo(THEME_DESC);
//                    assertThat(reservation.getTheme().getPrice()).isEqualTo(THEME_PRICE);
//                });
//    }
//
//    @Test
//    void deleteReservation() {
//        reservationDAO.deleteReservation(1L);
//
//        Long count = jdbcTemplate.queryForObject("SELECT count(*) FROM RESERVATION", Long.class);
//        assertThat(count).isEqualTo(0L);
//    }
//
//    @Test
//    void findCountReservationByDateTime() {
//        Long count = reservationDAO.findCountReservationByDateTime(DATE, TIME);
//
//        assertThat(count).isEqualTo(1L);
//    }
}

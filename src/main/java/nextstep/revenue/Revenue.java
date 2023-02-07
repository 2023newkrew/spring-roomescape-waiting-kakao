package nextstep.revenue;

public class Revenue {
    private Long id;
    private final Long reservationId;
    private final int price;

    public Revenue(final Long id, final Long reservationId, final int price) {
        this.id = id;
        this.reservationId = reservationId;
        this.price = price;
    }

    public Revenue(final Long reservationId, final int price) {
        this.reservationId = reservationId;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public int getPrice() {
        return price;
    }

    public static RevenueBuilder builder() {
        return new RevenueBuilder();
    }

    public static class RevenueBuilder {
        private Long id;
        private Long reservationId;
        private int price;

        private RevenueBuilder() {
        }

        public RevenueBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public RevenueBuilder reservationId(Long reservationId) {
            this.reservationId = reservationId;
            return this;
        }

        public RevenueBuilder price(int price) {
            this.price = price;
            return this;
        }

        public Revenue build() {
            return new Revenue(id, reservationId, price);
        }
    }
}

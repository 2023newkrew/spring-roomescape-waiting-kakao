package nextstep.waiting;

public class WaitingRegisterStatus {
    private final Long id;
    private final boolean isWaiting;

    public WaitingRegisterStatus(Long id, boolean isWaiting) {
        this.id = id;
        this.isWaiting = isWaiting;
    }

    public static WaitingRegisterStatus ofWaiting(Long id) {
        return new WaitingRegisterStatus(id, true);
    }

    public static WaitingRegisterStatus ofReservation(Long id) {
        return new WaitingRegisterStatus(id, false);
    }

    public boolean isRegisteredAsWaiting() {
        return isWaiting;
    }

    public Long getId() {
        return id;
    }
}

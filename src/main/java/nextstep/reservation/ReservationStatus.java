package nextstep.reservation;

public enum ReservationStatus {

    UNAPPROVED, APPROVED, CANCELED, WAIT_CANCEL, REJECTED;

    public boolean isUnapproved() {
        return this.equals(ReservationStatus.UNAPPROVED);
    }

    public boolean isApproved() {
        return this.equals(ReservationStatus.APPROVED);
    }

    public boolean isWaitCancel() {
        return this.equals(ReservationStatus.WAIT_CANCEL);
    }
}

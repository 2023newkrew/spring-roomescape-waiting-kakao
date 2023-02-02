package roomwaiting.support;

public enum Messages {
    LOGIN_NEEDS("Needs to be login"),
    EMPTY_TOKEN("token must have a value"),
    PASSWORD_INCORRECT("Password incorrect"),
    EMPTY_VALUE("Value must not be empty"),
    MEMBER_NOT_FOUND("Not found member"),
    THEME_NOT_FOUND("Not found theme"),
    SCHEDULE_NOT_FOUND("Not found schedule"),
    RESERVATION_NOT_FOUND("Not found reservation"),
    RESERVATION_WAITING_NOT_FOUND("Not found reservation waiting"),
    ID(", Id: "),
    USERNAME(", Username: "),
    OR(", or "),
    INVALID_TOKEN("Token don't match"),
    JWT_EXCEPTION("Token is not validate. ErrorMessage: "),
    ALREADY_USER("Already Registered User"),
    ALREADY_REGISTERED_RESERVATION("Reservation already exists"),
    ALREADY_REGISTERED_THEME("Theme already exists"),
    ALREADY_REGISTERED_SCHEDULE("Schedule already exists"),
    NOT_PERMISSION_DELETE("Not permission to delete"),
    NOT_ALLOWED_SERVICE("Only allowed to ADMIN USERS"),
    CREATE_USER("User Create Success, Username: "),
    UPDATE_ADMIN("Users Role updated to Admin, Username: "),
    ALREADY_ADMIN("Role of member already admin level"),
    NEEDS_NOT_APPROVED_STATUS("Status should be NotApproved"),
    NEEDS_APPROVED_STATUS("Status should be Approved"),
    EMPTY_SALES("There's No Sales"),

    ;

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

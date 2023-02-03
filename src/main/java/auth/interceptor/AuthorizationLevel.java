package auth.interceptor;

public enum AuthorizationLevel {
    ADMIN,
    USER;

    @Override
    public String toString() {
        return this.name();
    }

    public static AuthorizationLevel fromString(String authString) throws IllegalArgumentException {
        return AuthorizationLevel.valueOf(authString);
    }
}

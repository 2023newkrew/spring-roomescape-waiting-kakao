package auth.config;

public interface MemberDetails {
    Long getId();

    String getUsername();

    String getPassword();

    String getName();

    String getPhone();

    String getRole();

    boolean checkWrongPassword(String password);
}

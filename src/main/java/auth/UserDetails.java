package auth;

public class UserDetails {
    protected Long id;
    protected String username;
    protected String password;
    protected String name;
    protected String phone;
    protected String role;

    public UserDetails() {
    }

    public UserDetails(Long id, String username, String password, String name, String phone, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public UserDetails(String username, String password, String name, String phone, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public boolean checkWrongPassword(String password) {
        return !this.password.equals(password);
    }
}

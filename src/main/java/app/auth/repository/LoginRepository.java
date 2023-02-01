package app.auth.repository;

import app.auth.domain.User;

public interface LoginRepository {
    public User findById(Long id);
    public User findByUsername(String username);
}

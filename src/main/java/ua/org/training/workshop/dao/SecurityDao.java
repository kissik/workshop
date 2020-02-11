package ua.org.training.workshop.dao;

public interface SecurityDao {
    String findPasswordByUsername(String username);
}

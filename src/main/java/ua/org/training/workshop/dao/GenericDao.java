package ua.org.training.workshop.dao;

import ua.org.training.workshop.utilities.Pageable;

import java.sql.SQLException;
import java.util.Optional;

public interface GenericDao<T> extends AutoCloseable {
    void create (T entity) throws SQLException;
    Optional<T> findById(Long id);
    Pageable getPage(Pageable page);
    void update(T entity) throws SQLException;
    void delete(Long id);
    void close();
}


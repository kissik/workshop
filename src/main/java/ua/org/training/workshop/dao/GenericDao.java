package ua.org.training.workshop.dao;

import ua.org.training.workshop.utility.Page;

import java.sql.SQLException;
import java.util.Optional;

public interface GenericDao<T> extends AutoCloseable {
    Long create(T entity) throws SQLException;

    Optional<T> findById(Long id);

    Page<T> getPage(Page<T> page);

    void update(T entity) throws SQLException;

    void delete(Long id) throws SQLException;

    void close();
}


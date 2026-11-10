package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.exception.BackendException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface Dao<T, S> {
    default List<T> getAll() throws BackendException {
        return new ArrayList<>();
    }

    default Optional<T> get(S id) throws BackendException {
        return Optional.empty();
    }

    default boolean create(T entity) throws BackendException {
        return false;
    }

    default boolean update(T entity) throws BackendException {
        return false;
    }

    default boolean delete(T entity) throws BackendException {
        return false;
    }
}

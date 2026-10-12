package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.exception.BackendException;

import java.util.List;
import java.util.Optional;

public interface Dao<T, S> {
    Optional<T> get(S id) throws BackendException;
    boolean create(T entity) throws BackendException;
    boolean update(T entity) throws BackendException;
    boolean delete(S id) throws BackendException;
    List<T> getAll() throws BackendException;
}

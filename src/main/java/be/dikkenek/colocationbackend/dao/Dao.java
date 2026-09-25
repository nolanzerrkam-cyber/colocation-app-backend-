package be.dikkenek.colocationbackend.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T, S> {
    List<T> getAll();
    Optional<T> get(S id);
    boolean create(T entity);
    boolean update(T entity);
    boolean delete(S id);
}

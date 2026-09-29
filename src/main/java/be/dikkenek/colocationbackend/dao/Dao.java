package be.dikkenek.colocationbackend.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface Dao<T, S> {
    default List<T> getAll() {
        return new ArrayList<>();
    };
    default Optional<T> get(S id) {
        return Optional.empty();
    };
    default boolean create(T entity) {
        return false;
    };
    default boolean update(T entity) {
        return false;
    };
    default boolean delete(S id) {
        return false;
    };
}

package library.repository.base;

import java.util.Optional;

public interface IGenericRepository<T extends IEntity, C> {
    void add(T entity);
    void delete(T entity);
    void delete(int id);
    void update(T entity);
    Optional<C> getAll();
    Optional<T> getById(int id);
}

package library.repository.base;

import library.domain.Book;
import library.domain.BorrowRecord;
import library.domain.Member;
import library.storage.DataStorage;
import java.util.Optional;

public abstract class GenericRepository<T extends IEntity, C> implements IGenericRepository<T, C> {

    protected abstract EntityType getEntityType();

    protected enum EntityType{
        BOOK, MEMBER, BORROW_RECORD
    }

    private int getNextId() {
        return switch (getEntityType()) {
            case BOOK -> DataStorage.getNextBookId();
            case MEMBER -> DataStorage.getNextMemberId();
            case BORROW_RECORD -> DataStorage.getNextBorrowId();
        };
    }

    public abstract void update(T entity);
    public abstract Optional<C> getAll();
    public abstract Optional<T> getById(int id);

    @Override
    public void add(T entity) {
        entity.setId(getNextId());
        switch (getEntityType()) {
            case BOOK -> DataStorage.getBookList().add((Book) entity);
            case MEMBER -> DataStorage.getMembers().add((Member) entity);
            case BORROW_RECORD -> DataStorage.getBorrowRecords()
                    .put(entity.getId(), (BorrowRecord) entity);
        }
    }

    @Override
    public void delete(T entity){
        try {

            switch (getEntityType()){
                case BOOK -> DataStorage.getBookList().remove((Book) entity);
                case MEMBER -> DataStorage.getMembers().remove((Member) entity);
                case BORROW_RECORD -> DataStorage.getBorrowRecords()
                        .remove(((BorrowRecord) entity).getId(), (BorrowRecord) entity);
                default -> throw new IllegalStateException("Unexpected value: " + getEntityType());
            }
        }
        catch (ClassCastException e){
            throw new IllegalArgumentException("Invalid entity type for this repo ", e);
        }
    }
}

package library.repository;

import library.domain.Book;
import library.domain.enums.BookStatus;
import library.repository.base.GenericRepository;
import library.storage.DataStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BookRepository extends GenericRepository<Book, List<Book>> implements IBookRepository {

    @Override
    protected EntityType getEntityType() {
        return EntityType.BOOK;
    }

    public void delete(int id) {
        Book book = DataStorage.getBookList().stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElse(null);
        if (book != null) {
            DataStorage.getBookList().remove(book);
        }
    }
    @Override
    public void update(Book entity) {
        int index = IntStream.range(0, DataStorage.getBookList().size())
                .filter(i -> DataStorage.getBookList().get(i).getId() == entity.getId())
                .findFirst()
                .orElse(-1);
        if (index != -1) {
            DataStorage.getBookList().set(index, entity);
        }
    }

    @Override
    public Optional<List<Book>> getAll() {
        return Optional.of(new ArrayList<>(DataStorage.getBookList()));
    }

    @Override
    public Optional<Book> getById(int id) {
        return DataStorage.getBookList().stream()
                .filter(b -> b.getId() == id)
                .findFirst();
    }


    @Override
    public List<Book> findByName(String name) {
        return DataStorage.getBookList().stream()
                .filter(b -> b.getName().equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByStatus(BookStatus status) {
        return DataStorage.getBookList().stream()
                .filter(e -> e.getStatus().equals(status.getDisplayName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findAvailableBooks() {
        return DataStorage.getBookList().stream()
                .filter(book -> book.getStatus().equals("Available"))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return DataStorage.getBookList().stream()
                .filter(b -> b.getAuthor().equals(author))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByCategory(String category) {
        return DataStorage.getBookList().stream()
                .filter(b -> b.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByPublisher(String publisher) {
        return DataStorage.getBookList().stream()
                .filter(b -> b.getPublisher().equals(publisher))
                .collect(Collectors.toList());
    }
}

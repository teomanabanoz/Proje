package library.repository;

import library.domain.Book;
import library.domain.enums.BookStatus;
import library.repository.base.IGenericRepository;
import java.util.List;

public interface IBookRepository extends IGenericRepository<Book, List<Book>> {
    List<Book> findByAuthor(String author);
    List<Book> findByCategory(String category);
    List<Book> findByPublisher(String publisher);
    List<Book> findByName(String name);
    List<Book> findByStatus(BookStatus status);
    List<Book> findAvailableBooks();
}

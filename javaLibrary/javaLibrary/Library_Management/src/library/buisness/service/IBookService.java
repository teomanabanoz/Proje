package library.buisness.service;

import library.domain.Book;
import library.domain.enums.BookStatus;
import java.util.List;

public interface IBookService {
    void addBook(Book book);
    void updateBook(Book book);
    void deleteBook(int id);
    Book getBookById(int id);
    List<Book> getAllBooks();
    List<Book> findBooksByName(String name);
    List<Book> findBooksByStatus(BookStatus status);
    List<Book> findAvailableBooks();
    List<Book> findBooksByAuthor(String author);
    List<Book> findBooksByCategory(String category);
    List<Book> findBooksByPublisher(String publisher);
}

package library.buisness.service;

import library.domain.Book;
import library.domain.enums.BookStatus;
import library.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;

public class BookService implements IBookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        bookRepository.add(book);
    }

    @Override
    public void updateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        bookRepository.update(book);
    }

    @Override
    public void deleteBook(int id) {
        bookRepository.delete(id);
    }

    @Override
    public Book getBookById(int id) {
        return bookRepository.getById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    @Override
    public List<Book> getAllBooks() {
        return new ArrayList<>(bookRepository.getAll().orElseThrow(() -> new RuntimeException("No books found")));
    }

    @Override
    public List<Book> findBooksByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        return bookRepository.findByName(name);
    }

    @Override
    public List<Book> findBooksByStatus(BookStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        return bookRepository.findByStatus(status);
    }

    @Override
    public List<Book> findAvailableBooks() {
        return bookRepository.findAvailableBooks();
    }

    @Override
    public List<Book> findBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        return bookRepository.findByAuthor(author);
    }

    @Override
    public List<Book> findBooksByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        return bookRepository.findByCategory(category);
    }

    @Override
    public List<Book> findBooksByPublisher(String publisher) {
        if (publisher == null || publisher.trim().isEmpty()) {
            throw new IllegalArgumentException("Publisher cannot be null or empty");
        }
        return bookRepository.findByPublisher(publisher);
    }
}

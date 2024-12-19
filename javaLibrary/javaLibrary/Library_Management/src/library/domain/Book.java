package library.domain;

import library.domain.enums.BookCategory;
import library.domain.enums.BookStatus;
import library.repository.base.IEntity;

public class Book implements IEntity {
    private int Id;
    private String Name;
    private String author;
    private BookCategory category;
    private String publisher;
    private BookStatus status;


    public Book() { }

    //setters
    public void setId(int bookId) {
        this.Id = bookId;
    }
    public void setName(String bookName) {
        this.Name = bookName;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public void setStatus(BookStatus status) {
        this.status = status;
    }
    public void setCategory(BookCategory category) {
        this.category = category;
    }

    //getters
    public int getId() {
        return Id;
    }
    public String getName() {
        return Name;
    }
    public String getAuthor() {
        return author;
    }
    public String getPublisher() {
        return publisher;
    }
    public String getStatus() {
        return status.getDisplayName();
    }
    public String getCategory() {
        return category.getDisplayName();
    }
}

package library.domain;

import library.repository.base.IEntity;
import java.time.LocalDate;

public class BorrowRecord implements IEntity {
    private int id;
    private Book book;
    private Member member;
    private final LocalDate borrowDate = LocalDate.now();
    private LocalDate dueDate = borrowDate.plusDays(7);
    private LocalDate returnDate;
    private boolean isReturned;


    public BorrowRecord() {}


    //getters
    public int getId() {
        return id;
    }
    public Book getBook() {
        return book;
    }
    public Member getMember() {
        return member;
    }
    public LocalDate getBorrowDate() {
        return borrowDate;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public LocalDate getReturnDate() {
        return returnDate;
    }
    public boolean getIsReturned() {
        return isReturned;
    }


    //setters
    public void setId(int id) {
        this.id = id;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public void setMember(Member member) {
        this.member = member;
    }

    public void setReturnDate() {
        this.returnDate = LocalDate.now();
    }
    public void setIsReturned(boolean isReturned) {
        this.isReturned = isReturned;
    }
}

package library.storage;

import library.domain.Book;
import library.domain.BorrowRecord;
import library.domain.Member;
import java.util.*;

public class DataStorage {

    private static List<Book> bookList = new ArrayList<>();
    private static Set<Member> members = new HashSet<>();
    private static Map<Integer, BorrowRecord> borrowRecords = new HashMap<>();

    private static int nextBookId = 1;
    private static int nextMemberId = 1;
    private static int nextBorrowId = 1;

    public static List<Book> getBookList() {
        return bookList;
    }

    public static Set<Member> getMembers() {
        return members;
    }

    public static Map<Integer, BorrowRecord> getBorrowRecords() {
        return borrowRecords;
    }

    public static synchronized int getNextBookId() {
        return nextBookId++;
    }
    public static synchronized int getNextMemberId() {
        return nextMemberId++;
    }
    public static synchronized int getNextBorrowId() {
        return nextBorrowId++;
    }
}

package library.buisness.service;

import library.domain.BorrowRecord;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IBorrowRecordService {
    void addBorrowRecord(BorrowRecord borrowRecord);
    void updateBorrowRecord(BorrowRecord borrowRecord);
    void deleteBorrowRecord(int id);
    BorrowRecord getBorrowRecordById(int id);
    Map<Integer, BorrowRecord> getAllBorrowRecords();
    Map<Integer, BorrowRecord> getActiveBorrows();
    Map<Integer, BorrowRecord> getOverdueBorrows();
    List<BorrowRecord> getBorrowRecordsByMemberId(int memberId);
    List<BorrowRecord> getActiveBorrowRecordsByMemberId(int memberId);
    BorrowRecord getActiveBookBorrow(int bookId);
    List<BorrowRecord> getBorrowsByBookId(int bookId);
    List<BorrowRecord> getBorrowsByDateRange(LocalDate startDate, LocalDate endDate);
    int getActiveBorrowsCount();
    int getOverdueBorrowsCount();
}

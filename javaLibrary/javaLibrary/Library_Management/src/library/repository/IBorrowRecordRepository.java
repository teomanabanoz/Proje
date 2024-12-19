package library.repository;

import library.domain.BorrowRecord;
import library.repository.base.IGenericRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IBorrowRecordRepository extends IGenericRepository<BorrowRecord, Map<Integer, BorrowRecord>> {

    Map<Integer, BorrowRecord> findActiveBorrows();
    Map<Integer, BorrowRecord> findOverdueBorrows();

    List<BorrowRecord> findBorrowRecordsByMemberId(int id);
    List<BorrowRecord> findActiveBorrowRecordsByMemberId(int id);

    Optional<BorrowRecord> findActiveBookBorrow(int bookId);
    List<BorrowRecord> findByBookId(int bookId);

    List<BorrowRecord> findBorrowsByDateRange(LocalDate startDate, LocalDate endDate);

    int activeBorrowsCount();
    int overdueBorrowsCount();

}
